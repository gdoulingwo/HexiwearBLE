package link_work.hexiwearble.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import link_work.hexiwearble.R;

public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private List<BleDevice> bleDeviceList;
    private OnDeviceClickListener mListener;

    public DeviceAdapter(Context context) {
        this.context = context;
        bleDeviceList = new ArrayList<>();
    }

    public void addDevice(BleDevice bleDevice) {
        removeDevice(bleDevice);
        bleDeviceList.add(bleDevice);
    }

    public void removeDevice(BleDevice bleDevice) {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            BleDevice device = bleDeviceList.get(i);
            if (bleDevice.getKey().equals(device.getKey())) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clearConnectedDevice() {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            BleDevice device = bleDeviceList.get(i);
            if (BleManager.getInstance().isConnected(device)) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clearScanDevice() {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            BleDevice device = bleDeviceList.get(i);
            if (!BleManager.getInstance().isConnected(device)) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clear() {
        clearConnectedDevice();
        clearScanDevice();
    }

    @Override
    public int getCount() {
        return bleDeviceList.size();
    }

    @Override
    public BleDevice getItem(int position) {
        if (position > bleDeviceList.size()) {
            return null;
        }
        return bleDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.adapter_device, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.imgBlue = convertView.findViewById(R.id.img_blue);
            holder.txtName = convertView.findViewById(R.id.txt_name);
            holder.txtMac = convertView.findViewById(R.id.txt_mac);
            holder.txtRssi = convertView.findViewById(R.id.txt_rssi);
            holder.layoutIdle = convertView.findViewById(R.id.layout_idle);
            holder.layoutConnected = convertView.findViewById(R.id.layout_connected);
            holder.btnDisconnect = convertView.findViewById(R.id.btn_disconnect);
            holder.btnConnect = convertView.findViewById(R.id.btn_connect);
            holder.btnDetail = convertView.findViewById(R.id.btn_detail);
        }

        final BleDevice bleDevice = getItem(position);
        if (bleDevice != null) {
            boolean isConnected = BleManager.getInstance().isConnected(bleDevice);
            String name = bleDevice.getName();
            String mac = bleDevice.getMac();
            int rssi = bleDevice.getRssi();
            holder.txtName.setText(name);
            holder.txtMac.setText(mac);
            holder.txtRssi.setText(String.valueOf(rssi));
            if (isConnected) {
                holder.imgBlue.setImageResource(R.drawable.connected);
//                holder.txtName.setTextColor(0xFF1DE9B6);
//                holder.txtMac.setTextColor(0xFF1DE9B6);
                holder.txtName.setTextColor(0xFF0065D6);
                holder.txtMac.setTextColor(0xFF0065D6);
                holder.layoutIdle.setVisibility(View.GONE);
                holder.layoutConnected.setVisibility(View.VISIBLE);
            } else {
                holder.imgBlue.setImageResource(R.mipmap.ic_blue_remote);
                holder.txtName.setTextColor(0xFF000000);
                holder.txtMac.setTextColor(0xFF000000);
                holder.layoutIdle.setVisibility(View.VISIBLE);
                holder.layoutConnected.setVisibility(View.GONE);
            }
        }

        holder.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConnect(bleDevice);
                }
            }
        });

        holder.btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDisConnect(bleDevice);
                }
            }
        });

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDetail(bleDevice);
                }
            }
        });

        return convertView;
    }

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }

    public interface OnDeviceClickListener {
        void onConnect(BleDevice bleDevice);

        void onDisConnect(BleDevice bleDevice);

        void onDetail(BleDevice bleDevice);
    }

    class ViewHolder {
        ImageView imgBlue;
        TextView txtName;
        TextView txtMac;
        TextView txtRssi;
        LinearLayout layoutIdle;
        LinearLayout layoutConnected;
        Button btnDisconnect;
        Button btnConnect;
        Button btnDetail;
    }

}
