package yuan.core.http;


import androidx.annotation.NonNull;

/**
 * @author yuanye
 * @date 2019/6/27
 */
public interface IRequestBody<RequestBody> {

    RequestBody addParams(String key, String value);

    RequestBody addHead(String key, String value);

    RequestBody addJson(@NonNull String json);

    RequestBody addByte(@NonNull byte[] bytes1);

    RequestBody addFile(@NonNull String key, @NonNull String filePath);

    void execute();

}
