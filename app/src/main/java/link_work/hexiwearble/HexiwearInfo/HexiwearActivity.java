package link_work.hexiwearble.HexiwearInfo;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;

import link_work.hexiwearble.Model.Characteristic;
import link_work.hexiwearble.R;
import link_work.hexiwearble.Util.DataConverter;

import static link_work.hexiwearble.Util.CommonUtil.WRITE_TIME;

public class HexiwearActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY_DATA = "key_data";
    private static final String TAG = "HexiwearActivity";
    private static final Queue<Characteristic> READING_QUEUE = new ArrayBlockingQueue<>(4);
    private HashMap<Characteristic, BluetoothGattCharacteristic> characteristicList;
    private BleDevice bleDevice;
    private int isFirst;
    private boolean shouldUpdateTime;

    private TextView battery;
    private TextView heartRate;
    private TextView temperature;
    private TextView humidity;
    private TextView pressure;
    private Button updateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexiwear);
        initView();
        initData();
        initListener();
        readNextCharacteristics();
    }

    private void initListener() {
        updateTime.setOnClickListener(this);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("手环信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        battery = findViewById(R.id.battery);
        heartRate = findViewById(R.id.heart_rate);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        updateTime = findViewById(R.id.update);
    }

    private void initData() {
        shouldUpdateTime = true;

        bleDevice = getIntent().getParcelableExtra(KEY_DATA);

        characteristicList = new HashMap<>(12);
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        for (BluetoothGattService service : gatt.getServices()) {
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                Characteristic temp = Characteristic.byUuid(characteristic.getUuid().toString());
                if (temp != null) {
                    characteristicList.put(temp, characteristic);
                }
            }
        }
        Log.i(TAG, "initData: " + characteristicList.size());

        setReadingQueue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().clearCharacterCallback(bleDevice);
    }

    private void setReadingQueue() {
        READING_QUEUE.clear();
        READING_QUEUE.add(Characteristic.TEMPERATURE);
        READING_QUEUE.add(Characteristic.HUMIDITY);
        READING_QUEUE.add(Characteristic.PRESSURE);
        READING_QUEUE.add(Characteristic.BATTERY);
        isFirst = READING_QUEUE.size();
    }

    void readNextCharacteristics() {
        final Characteristic characteristic = READING_QUEUE.poll();
        READING_QUEUE.add(characteristic);
        bleRead(bleDevice, characteristic);
    }

    private void bleRead(BleDevice bleDevice,
                         final Characteristic characteristic) {
        final BluetoothGattCharacteristic gattCharacteristic =
                characteristicList.get(characteristic);
        BleManager.getInstance().read(
                bleDevice,
                gattCharacteristic.getService().getUuid().toString(),
                gattCharacteristic.getUuid().toString(),
                new BleReadCallback() {

                    @Override
                    public void onReadSuccess(final byte[] data) {
                        setText(gattCharacteristic,
                                DataConverter.parseBluetoothData(characteristic, data));
                        try {
                            // 等待时间，如果不是第一个加载的话，就睡眠两秒钟。
                            if (isFirst < 0) {
                                Thread.sleep(2333);
                            } else {
                                if (isFirst > 0) {
                                    --isFirst;
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (shouldUpdateTime) {
                            updateTime();
                        }

                        readNextCharacteristics();
                    }

                    @Override
                    public void onReadFailure(final BleException exception) {
                        // TODO
                    }
                });
    }

    private void setText(BluetoothGattCharacteristic characteristic, String string) {
        if (TextUtils.equals(characteristic.getUuid().toString(), Characteristic.TEMPERATURE.getUuid())) {
            setRealString(temperature, "温度为：" + string);
            Log.i(TAG, "onReadSuccess: 温度为：" + string);
        } else if (TextUtils.equals(characteristic.getUuid().toString(), Characteristic.HUMIDITY.getUuid())) {
            setRealString(humidity, "相对湿度为：" + string);
            Log.i(TAG, "onReadSuccess: 相对湿度为：" + string);
        } else if (TextUtils.equals(characteristic.getUuid().toString(), Characteristic.PRESSURE.getUuid())) {
            setRealString(pressure, "大气压强为：" + string);
            Log.i(TAG, "onReadSuccess: 大气压强为：" + string);
        } else if (TextUtils.equals(characteristic.getUuid().toString(), Characteristic.BATTERY.getUuid())) {
            setRealString(battery, "电量为：" + string);
            Log.i(TAG, "onReadSuccess: 电量为：" + string);
        }
    }

    private void setRealString(TextView textView, String string) {
        textView.setText(string);
    }

    private void bleWriteWithoutResponse(BleDevice bleDevice,
                                         BluetoothGattCharacteristic characteristic,
                                         byte[] hex) {
        bleWrite(bleDevice, characteristic, hex);
    }

    private void bleStopNotify(BleDevice bleDevice, BluetoothGattCharacteristic characteristic) {
        BleManager.getInstance().stopNotify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString());
    }

    private void bleStopIndicate(BleDevice bleDevice, BluetoothGattCharacteristic characteristic) {
        BleManager.getInstance().stopIndicate(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString());
    }

    private void bleStartIndicate(BleDevice bleDevice,
                                  final BluetoothGattCharacteristic characteristic,
                                  final TextView txt) {
        BleManager.getInstance().indicate(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleIndicateCallback() {

                    @Override
                    public void onIndicateSuccess() {

                    }

                    @Override
                    public void onIndicateFailure(final BleException exception) {

                    }

                    @Override
                    public void onCharacteristicChanged(final byte[] data) {

                    }
                });
    }

    private void bleWrite(BleDevice bleDevice,
                          BluetoothGattCharacteristic characteristic, byte[] hex) {
        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                hex,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess() {
                        Log.i(TAG, "onWriteSuccess: 设置时间成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(HexiwearActivity.this, "设置时间成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        readNextCharacteristics();
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        Log.i(TAG, "onWriteFailure: " + exception);
                    }
                });
    }

    private void bleStartNotify(BleDevice bleDevice,
                                final BluetoothGattCharacteristic characteristic,
                                final TextView txt) {
        BleManager.getInstance().notify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {

                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String temp = txt.getText().toString().replaceAll("\\d", "")
                                        + HexUtil.formatHexString(characteristic.getValue());
                                Log.i(TAG, "run: changed -> " + temp);
                                txt.setText(temp);
                            }
                        });
                    }
                });
    }

    void updateTime() {
        shouldUpdateTime = false;

        final byte[] time = new byte[20];
        final long currentTime = System.currentTimeMillis();
        final long currentTimeWithTimeZoneOffset = (currentTime + TimeZone.getDefault().getOffset(currentTime)) / 1000;

        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN).asLongBuffer().put(currentTimeWithTimeZoneOffset);
        final byte[] utcBytes = buffer.array();

        final byte length = 0x04;

        time[0] = WRITE_TIME;
        time[1] = length;
        time[2] = utcBytes[0];
        time[3] = utcBytes[1];
        time[4] = utcBytes[2];
        time[5] = utcBytes[3];

        bleWrite(bleDevice, characteristicList.get(Characteristic.ALERT_IN), time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                shouldUpdateTime = true;
                break;
            default:
                break;
        }
    }
}
