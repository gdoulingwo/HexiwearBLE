package link_work.hexiwearble.operation;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.ArrayList;
import java.util.List;

import link_work.hexiwearble.R;
import link_work.hexiwearble.Util.CommonUtil;
import link_work.hexiwearble.Util.DataConverter;

/**
 * 操作控制台
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CharacteristicOperationFragment extends Fragment {

    public static final int PROPERTY_READ = 1;
    public static final int PROPERTY_WRITE = 2;
    public static final int PROPERTY_WRITE_NO_RESPONSE = 3;
    public static final int PROPERTY_NOTIFY = 4;
    public static final int PROPERTY_INDICATE = 5;

    private LinearLayout layoutContainer;

    private List<String> childList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_characteric_operation, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        layoutContainer = v.findViewById(R.id.layout_container);
    }

    public void showData() {
        final BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        final BluetoothGattCharacteristic characteristic = ((OperationActivity) getActivity()).getCharacteristic();
        final int charaProp = ((OperationActivity) getActivity()).getCharaProp();
        String child = characteristic.getUuid().toString() + String.valueOf(charaProp);

        for (int i = 0; i < layoutContainer.getChildCount(); i++) {
            layoutContainer.getChildAt(i).setVisibility(View.GONE);
        }
        if (childList.contains(child)) {
            layoutContainer.findViewWithTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp).setVisibility(View.VISIBLE);
        } else {
            childList.add(child);

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation, null);
            view.setTag(bleDevice.getKey() + characteristic.getUuid().toString() + charaProp);
            LinearLayout layoutAdd = view.findViewById(R.id.layout_add);
            final TextView txtTitle = view.findViewById(R.id.txt_title);
            txtTitle.setText(String.valueOf(characteristic.getUuid().toString() + getActivity().getString(R.string.data_changed)));
            final TextView txt = view.findViewById(R.id.txt);
            txt.setMovementMethod(ScrollingMovementMethod.getInstance());

            switch (charaProp) {
                case PROPERTY_READ: {
                    View viewAdd = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    Button btn = viewAdd.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.read));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bleRead(bleDevice, characteristic, txt);
                        }
                    });
                    layoutAdd.addView(viewAdd);
                }
                break;

                case PROPERTY_WRITE: {
                    View viewAdd = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = viewAdd.findViewById(R.id.et);
                    Button btn = viewAdd.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }
                            bleWrite(bleDevice, characteristic, txt, hex);
                        }
                    });
                    layoutAdd.addView(viewAdd);
                }
                break;

                case PROPERTY_WRITE_NO_RESPONSE: {
                    View viewAdd = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_et, null);
                    final EditText et = viewAdd.findViewById(R.id.et);
                    Button btn = viewAdd.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.write));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String hex = et.getText().toString();
                            if (TextUtils.isEmpty(hex)) {
                                return;
                            }
                            bleWriteWithoutResponse(bleDevice, characteristic, txt, hex);
                        }
                    });
                    layoutAdd.addView(viewAdd);
                }
                break;

                case PROPERTY_NOTIFY: {
                    View viewAdd = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = viewAdd.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                bleStartNotify(bleDevice, characteristic, txt);
                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                bleStopNotify(bleDevice, characteristic);
                            }
                        }
                    });
                    layoutAdd.addView(viewAdd);
                }
                break;

                case PROPERTY_INDICATE: {
                    View viewAdd = LayoutInflater.from(getActivity()).inflate(R.layout.layout_characteric_operation_button, null);
                    final Button btn = viewAdd.findViewById(R.id.btn);
                    btn.setText(getActivity().getString(R.string.open_notification));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().toString().equals(getActivity().getString(R.string.open_notification))) {
                                btn.setText(getActivity().getString(R.string.close_notification));
                                bleStartIndicate(bleDevice, characteristic, txt);
                            } else {
                                btn.setText(getActivity().getString(R.string.open_notification));
                                bleStopIndicate(bleDevice, characteristic);
                            }
                        }
                    });
                    layoutAdd.addView(viewAdd);
                }
                break;
                default:
                    break;
            }

            layoutContainer.addView(view);
        }
    }

    private void bleRead(BleDevice bleDevice,
                         final BluetoothGattCharacteristic characteristic,
                         final TextView txt) {
        BleManager.getInstance().read(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleReadCallback() {

                    @Override
                    public void onReadSuccess(final byte[] data) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(DataConverter.parseBluetoothData(
                                            CommonUtil.NAME_CHAR.get(characteristic.getUuid().toString()),
                                            data
                                    ));
                                    // txt.append(HexUtil.formatHexString(data, true));
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onReadFailure(final BleException exception) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(exception.toString());
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void bleWriteWithoutResponse(BleDevice bleDevice,
                                         BluetoothGattCharacteristic characteristic,
                                         final TextView txt, String hex) {
        bleWrite(bleDevice, characteristic, txt, hex);
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
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append("indicate success");
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }

                    }

                    @Override
                    public void onIndicateFailure(final BleException exception) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(exception.toString());
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(DataConverter.parseBluetoothData(
                                            CommonUtil.NAME_CHAR.get(characteristic.getUuid().toString()),
                                            characteristic.getValue()
                                    ));
                                    // txt.append(HexUtil.formatHexString(characteristic.getValue(), true));
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void bleWrite(BleDevice bleDevice,
                          BluetoothGattCharacteristic characteristic,
                          final TextView txt, String hex) {
        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess() {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append("write success");
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(exception.toString());
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
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
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append("notify success");
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(exception.toString());
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt.append(DataConverter.parseBluetoothData(
                                            CommonUtil.NAME_CHAR.get(characteristic.getUuid().toString()),
                                            characteristic.getValue()
                                    ));
                                    // txt.append(HexUtil.formatHexString(characteristic.getValue(), true));
                                    txt.append("\n");
                                    int offset = txt.getLineCount() * txt.getLineHeight();
                                    if (offset > txt.getHeight()) {
                                        txt.scrollTo(0, offset - txt.getHeight());
                                    }
                                }
                            });
                        }
                    }
                });
    }

}
