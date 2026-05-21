package org.arrinna.nobugdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class ApiResult<T> {

    private boolean success;
    private Integer errCode;
    private String errMsg;
    private T data;

    /**
     * 然后还有成功返回和非成功返回的一些方法我们先省略
     *
     */

    /**
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ApiResult<T> success(T data){
       ApiResult apiResult=new ApiResult();
       apiResult.setSuccess(Boolean.TRUE);
       apiResult.setData(data);
       return apiResult;
    }

    /**
     *
     * @return
     * @param <T>
     */
    public static <T> ApiResult<T> success(){
        ApiResult apiResult=new ApiResult();
        apiResult.setSuccess(Boolean.TRUE);
        apiResult.setData(null);
        return apiResult;
    }

    /**
     *
     * @param errCode
     * @param errMsg
     * @return
     * @param <T>
     */
    public static <T> ApiResult<T> error(Integer errCode,String errMsg){
        ApiResult apiResult=new ApiResult();
        apiResult.setSuccess(Boolean.FALSE);
        apiResult.setErrMsg(errMsg);
        apiResult.setErrCode(errCode);
        return apiResult;
    }

}
