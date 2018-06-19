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
package GraphObj;

public interface doWork {
    void scan();//扫描

    void runCulAPC();

    void runCulAM();//主轴

    void runCulCore();

    void culBendCen(int x, int y);//弯曲中心

    void printResult();
}
