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

abstract class GraphHolder {
    int width;
    int height;

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }
}
