package yuan.core.http;

import java.io.IOException;

/**
 * 用于处理网络请求返回
 *
 * @author yuanye
 * @date 2019/6/9
 */
public interface Callback {

    void onFailure(IOException e);

    void onResponse(Response response);

}
