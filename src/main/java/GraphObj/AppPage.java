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

import java.io.File;

public class AppPage {
    public static void main(String[] args) {
        File graphSelect=new File(args[0]);
        BitmapHolder bitmapHolder=new BitmapHolder(graphSelect,1);
       bitmapHolder.scan();
       bitmapHolder.printResult();

    }

}
