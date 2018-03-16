package link_work.hexiwearble.Util;

import java.util.HashMap;
import java.util.Map;

import link_work.hexiwearble.Model.Characteristic;

/**
 * @author notzuonotdied
 * @date 18-1-4
 * @describe TODO
 */

public class CommonUtil {
    public static final byte WRITE_TIME = 3;
    public static final Map<String, String> NAME = new HashMap<>();
    public static final Map<String, Characteristic> NAME_CHAR = new HashMap<>();
    private static final byte WRITE_NOTIFICATION = 1;

    static {
        NAME.put(Characteristic.HEALTH_SERVICE.getUuid(), "健康服务");
        NAME.put(Characteristic.HEARTRATE.getUuid(), "心率");
        NAME.put(Characteristic.CALORIES.getUuid(), "卡路里");
        NAME.put(Characteristic.STEPS.getUuid(), "步数");

        NAME.put(Characteristic.SENSOR.getUuid(), "传感器");
        NAME.put(Characteristic.GYRO.getUuid(), "角度传感器");
        NAME.put(Characteristic.MAGNET.getUuid(), "三轴传感器");
        NAME.put(Characteristic.ACCELERATION.getUuid(), "加速度传感器");

        NAME.put(Characteristic.WEATHER.getUuid(), "天气服务器");
        NAME.put(Characteristic.LIGHT.getUuid(), "光强");
        NAME.put(Characteristic.TEMPERATURE.getUuid(), "温度");
        NAME.put(Characteristic.HUMIDITY.getUuid(), "相对湿度");
        NAME.put(Characteristic.PRESSURE.getUuid(), "压强");

        NAME.put(Characteristic.OTHER.getUuid(), "其他服务");
        NAME.put(Characteristic.BATTERY.getUuid(), "电量");

        NAME.put(Characteristic.APP_MODE_SERVICE.getUuid(), "模式服务");
        NAME.put(Characteristic.MODE.getUuid(), "手环模式");

        NAME.put(Characteristic.AC_SERVICE.getUuid(), "命令输入输出服务");
        NAME.put(Characteristic.ALERT_IN.getUuid(), "输入服务");
        NAME.put(Characteristic.ALERT_OUT.getUuid(), "输出服务");

        NAME.put(Characteristic.HARDWARE.getUuid(), "硬件版本服务");
        NAME.put(Characteristic.SERIAL.getUuid(), "SERIAL");
        NAME.put(Characteristic.FW_REVISION.getUuid(), "FW_REVISION");
        NAME.put(Characteristic.HW_REVISION.getUuid(), "HW_REVISION");
        NAME.put(Characteristic.MANUFACTURER.getUuid(), "MANUFACTURER");

        NAME.put(Characteristic.WHAT.getUuid(), "我也不知道是什么的服务");
        NAME.put(Characteristic.CONTROL_POINT.getUuid(), "CONTROL_POINT");
        NAME.put(Characteristic.DATA.getUuid(), "DATA");
        NAME.put(Characteristic.STATE.getUuid(), "STATE");

        NAME.put(Characteristic.PARAM.getUuid(), "设备信息服务");
        NAME.put(Characteristic.SIGNAL.getUuid(), "设备名称");
        NAME.put(Characteristic.Appearance.getUuid(), "Appearance");
        NAME.put(Characteristic.PERIPHERAL.getUuid(), "次要的连接参数");

        NAME.put(Characteristic.EMMM.getUuid(), "EMMM...");

        NAME_CHAR.put(Characteristic.HEALTH_SERVICE.getUuid(), Characteristic.HEALTH_SERVICE);
        NAME_CHAR.put(Characteristic.HEARTRATE.getUuid(), Characteristic.HEARTRATE);
        NAME_CHAR.put(Characteristic.CALORIES.getUuid(), Characteristic.CALORIES);
        NAME_CHAR.put(Characteristic.STEPS.getUuid(), Characteristic.STEPS);
        NAME_CHAR.put(Characteristic.SENSOR.getUuid(), Characteristic.SENSOR);
        NAME_CHAR.put(Characteristic.GYRO.getUuid(), Characteristic.GYRO);
        NAME_CHAR.put(Characteristic.MAGNET.getUuid(), Characteristic.MAGNET);
        NAME_CHAR.put(Characteristic.ACCELERATION.getUuid(), Characteristic.ACCELERATION);
        NAME_CHAR.put(Characteristic.WEATHER.getUuid(), Characteristic.WEATHER);
        NAME_CHAR.put(Characteristic.LIGHT.getUuid(), Characteristic.LIGHT);
        NAME_CHAR.put(Characteristic.TEMPERATURE.getUuid(), Characteristic.TEMPERATURE);
        NAME_CHAR.put(Characteristic.HUMIDITY.getUuid(), Characteristic.HUMIDITY);
        NAME_CHAR.put(Characteristic.PRESSURE.getUuid(), Characteristic.PRESSURE);
        NAME_CHAR.put(Characteristic.OTHER.getUuid(), Characteristic.OTHER);
        NAME_CHAR.put(Characteristic.BATTERY.getUuid(), Characteristic.BATTERY);
        NAME_CHAR.put(Characteristic.APP_MODE_SERVICE.getUuid(), Characteristic.APP_MODE_SERVICE);
        NAME_CHAR.put(Characteristic.MODE.getUuid(), Characteristic.MODE);
        NAME_CHAR.put(Characteristic.AC_SERVICE.getUuid(), Characteristic.AC_SERVICE);
        NAME_CHAR.put(Characteristic.ALERT_IN.getUuid(), Characteristic.ALERT_IN);
        NAME_CHAR.put(Characteristic.ALERT_OUT.getUuid(), Characteristic.ALERT_OUT);
        NAME_CHAR.put(Characteristic.HARDWARE.getUuid(), Characteristic.HARDWARE);
        NAME_CHAR.put(Characteristic.SERIAL.getUuid(), Characteristic.SERIAL);
        NAME_CHAR.put(Characteristic.FW_REVISION.getUuid(), Characteristic.FW_REVISION);
        NAME_CHAR.put(Characteristic.HW_REVISION.getUuid(), Characteristic.HW_REVISION);
        NAME_CHAR.put(Characteristic.MANUFACTURER.getUuid(), Characteristic.MANUFACTURER);
        NAME_CHAR.put(Characteristic.WHAT.getUuid(), Characteristic.WHAT);
        NAME_CHAR.put(Characteristic.CONTROL_POINT.getUuid(), Characteristic.CONTROL_POINT);
        NAME_CHAR.put(Characteristic.DATA.getUuid(), Characteristic.DATA);
        NAME_CHAR.put(Characteristic.STATE.getUuid(), Characteristic.STATE);
        NAME_CHAR.put(Characteristic.PARAM.getUuid(), Characteristic.PARAM);
        NAME_CHAR.put(Characteristic.SIGNAL.getUuid(), Characteristic.SIGNAL);
        NAME_CHAR.put(Characteristic.Appearance.getUuid(), Characteristic.Appearance);
        NAME_CHAR.put(Characteristic.PERIPHERAL.getUuid(), Characteristic.PERIPHERAL);
        NAME_CHAR.put(Characteristic.EMMM.getUuid(), Characteristic.EMMM);
    }
}
