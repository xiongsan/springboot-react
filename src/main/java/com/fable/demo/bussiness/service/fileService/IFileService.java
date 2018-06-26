package com.fable.demo.bussiness.service.fileService;

import com.fable.enclosure.bussiness.interfaces.BaseRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/1/23
 * Time :11:10
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public interface IFileService {

    String getFileFolder(HttpServletRequest request);

    BaseResponse deleteFile(BaseRequest<Map<String, String>> param);

    BaseResponse getFileList(BaseRequest<Map<String, String>> param);

    BaseResponse addFile(BaseRequest<Map<String, Object>> param);

    BaseResponse showPic();

}
