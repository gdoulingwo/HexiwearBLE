package link_work.hexiwearble.operation;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import link_work.hexiwearble.R;
import link_work.hexiwearble.Util.CommonUtil;

/**
 * 服务列表
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ServiceListFragment extends Fragment {
    private TextView txtName, txtMac;
    private ResultAdapter mResultAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_list, null);
        initView(v);
        showData();

        return v;
    }

    private void initView(View v) {
        txtName = v.findViewById(R.id.txt_name);
        txtMac = v.findViewById(R.id.txt_mac);

        mResultAdapter = new ResultAdapter(getActivity());
        ListView listviewDevice = v.findViewById(R.id.list_service);
        listviewDevice.setAdapter(mResultAdapter);
        listviewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothGattService service = mResultAdapter.getItem(position);
                ((OperationActivity) getActivity()).setBluetoothGattService(service);
                ((OperationActivity) getActivity()).changePage(1);
            }
        });
    }

    private void showData() {
        BleDevice bleDevice = ((OperationActivity) getActivity()).getBleDevice();
        String name = bleDevice.getName();
        String mac = bleDevice.getMac();
        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);

        txtName.setText(String.valueOf(getActivity().getString(R.string.name) + name));
        txtMac.setText(String.valueOf(getActivity().getString(R.string.mac) + mac));

        mResultAdapter.clear();
        for (BluetoothGattService service : gatt.getServices()) {
            mResultAdapter.addResult(service);
        }
        mResultAdapter.notifyDataSetChanged();
    }

    private class ResultAdapter extends BaseAdapter {
        private Context context;
        private List<BluetoothGattService> bluetoothGattServices;

        ResultAdapter(Context context) {
            this.context = context;
            bluetoothGattServices = new ArrayList<>();
        }

        void addResult(BluetoothGattService service) {
            bluetoothGattServices.add(service);
        }

        void clear() {
            bluetoothGattServices.clear();
        }

        @Override
        public int getCount() {
            return bluetoothGattServices.size();
        }

        @Override
        public BluetoothGattService getItem(int position) {
            if (position > bluetoothGattServices.size()) {
                return null;
            }
            return bluetoothGattServices.get(position);
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
                convertView = View.inflate(context, R.layout.adapter_service, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.txtTitle = convertView.findViewById(R.id.txt_title);
                holder.txtUuid = convertView.findViewById(R.id.txt_uuid);
                holder.txtType = convertView.findViewById(R.id.txt_type);
            }

            BluetoothGattService service = bluetoothGattServices.get(position);
            String uuid = service.getUuid().toString();
            Log.i("test", "getView: " + uuid);
            holder.txtTitle.setText("服务：" + CommonUtil.NAME.get(uuid));
            holder.txtUuid.setText(uuid);
            holder.txtType.setText(getActivity().getString(R.string.type));
            return convertView;
        }

        class ViewHolder {
            TextView txtTitle;
            TextView txtUuid;
            TextView txtType;
        }
    }

}
