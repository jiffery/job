package com.job.handler;


import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.job.contants.BusinessCodes;
import com.job.exception.JobException;
import com.job.util.DateUtil;
import com.job.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author  子羽
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        printLog(request, ex);
        ModelAndView mv = new ModelAndView();
        FastJsonJsonView view = new FastJsonJsonView();
        if (ex instanceof JobException) {
            JobException e = (JobException) ex;
                Map<String, Object> resp = JsonUtil.toRespJson(e.getErrorCode(), ex.getMessage());
                view.setAttributesMap(resp);
            mv.setView(view);
            return mv;
        } else {
            Map<String, Object> resp = JsonUtil.toRespJson(BusinessCodes.ERROR_CODE, BusinessCodes.ERROR_MSG);
            view.setAttributesMap(resp);
            mv.setView(view);
            return mv;
        }
    }


    private void printLog(HttpServletRequest request, Exception ex) {
        if (logger.isErrorEnabled()) {
            String uri = request.getRequestURI();
            Map<String, ?> parameterMap = request.getParameterMap();
            String prestr = "";
            List<String> keys = new ArrayList<String>(parameterMap.keySet());
            for (int i = 0; i < keys.size(); i++) {
                try {
                    String key = keys.get(i);
                    String[] values = (String[]) parameterMap.get(key);
                    String valueStr = values[0];
                    if (values.length > 1) {
                        valueStr = "[";
                        for (int j = 0; j < values.length; j++) {
                            valueStr += values[j];
                            valueStr += (j == values.length - 1) ? "" : ",";
                        }
                        valueStr = "]";
                    }
                    prestr += key + "=" + valueStr;
                    prestr += (i == keys.size() - 1) ? "" : "&";
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            logger.info("request Exception ,response time :" + DateUtil.getCurrentDateTime() + "  request uri:" + uri + " parameter :" + prestr, ex);
        }
    }
}