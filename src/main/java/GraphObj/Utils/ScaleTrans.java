/**
 * !!Please Don't edit this filehead.
 * Created with IntelliJ IDEA.
 * Created by @author ChangMin Zhao
 * Description:
 * User: ${USER}
 * Date: ${YEAR}-${MONTH}-${DAY}
 * Time: ${TIME}
 * Usage: ${PROJECT_NAME}
 */
package GraphObj.Utils;

public class ScaleTrans {


    private double DPmm = 11.811;//dot per mm
    //以上为默认值
    private double scale = 1.0;

    public ScaleTrans(double dpi) {
        DPmm = dpi / 25.4;
    }

    public ScaleTrans(double scale, double DPI) {
        if (DPI > 0) {
            this.DPmm = DPI / 25.4;
        }
        this.scale = scale;
    }

    public double bit2mm(double potnums) {
        return potnums / DPmm * scale;
    }//像素点转毫米
}
