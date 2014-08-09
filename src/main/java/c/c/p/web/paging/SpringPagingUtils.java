/**
 * 
 */
package c.c.p.web.paging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import c.c.p.Constants;
import c.c.p.utils.CollectionUtils;

/**
 * @author Administrator
 *
 */
public class SpringPagingUtils {
    
    private static Logger LOG = LoggerFactory.getLogger(SpringPagingUtils.class);

    public static <T> void paging(HttpServletRequest request, PagingExecute<T> execute) {
        paging(request, execute, null);
    }
    
    public static <T> void paging(HttpServletRequest request, PagingExecute<T> execute,Sort defaultSort) {
        PageRequest pageRequest = processPageRequest(request,defaultSort);
        request.setAttribute("page", execute.execute(pageRequest));
    }

    public static <T> Page<T> pagingData(HttpServletRequest request, PagingExecute<T> execute) {
        return pagingData(request,execute,null);
    }
    
    public static <T> Page<T> pagingData(HttpServletRequest request, PagingExecute<T> execute,Sort defaultSort) {
        PageRequest pageRequest = processPageRequest(request,defaultSort);
        return execute.execute(pageRequest);
    }
    
    public static PageRequest processPageRequest(HttpServletRequest request) {
        return processPageRequest(request, null);
    }

    public static PageRequest processPageRequest(HttpServletRequest request,Sort defaultSort) {

        Sort sort = null;

        int pageNumber = Constants.DEFAULT_PAGE;
        int pagzSize = Constants.DEFAULT_PAGE_SIZE;
        String sortName = "";
        String order = "";
        Map<String, String[]> params = new HashMap<String, String[]>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            if (key.equals(Constants.PARAMETE_PAGE)) {
                pageNumber = Integer.parseInt(map.get(key)[0]);
                LOG.debug("Page request paramete>pageNumber({})",pageNumber);
            } else if (Constants.PARAMETE_SORT.equals(key)) {
                sortName = map.get(key)[0];
                LOG.debug("Page request paramete>sortName({})",sortName);
            } else if (Constants.PARAMETE_ORDER.equals(key)) {
                order = map.get(key)[0];
                LOG.debug("Page request paramete>order({})",order);
            } else if (Constants.PARAMETE_PAGESIZE.equals(key)) {
                pagzSize = Integer.parseInt(map.get(key)[0]);
                LOG.debug("Page request paramete>pagzSize({})",pagzSize);
            } else if(Constants.PARAMETE_SELECTED_IDS.equals(key)){
                
                if(StringUtils.hasText(map.get(key)[0])) {
                    
                   Set<String> idset = new HashSet<String>();
                   for(String id: map.get(key)[0].split(",")) {
                       idset.add(id);
                   }
                   
                   String selectedIdsStr = CollectionUtils.join(idset, ",");
                   request.setAttribute("selectedIdSet", idset);
                   request.setAttribute("selectedIdsStr", selectedIdsStr);
                   LOG.debug("Page request paramete>selectedIdsStr({})",selectedIdsStr);
                }
                
            }  else if(Constants.PARAMETE_PAGE_CONTAINER.equals(key)){
                String pageContainer = map.get(key)[0];
                request.setAttribute(Constants.PARAMETE_PAGE_CONTAINER, pageContainer);
                LOG.debug("Page request paramete>pageContainer({})",pageContainer);
            } else {
                Object value = map.get(key);
                params.put(key, map.get(key));
                LOG.debug("Page request paramete>other ({}:{})",key,value);
            }
        }
        if (StringUtils.hasText(sortName) && StringUtils.hasText(order)) {
            sort = new Sort(Direction.fromString(order), sortName);
        } else {
            sort = defaultSort;
        }

        /**
         *  Mapper join >P
         */
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            for (String val : params.get(key)) {
                sb.append(key).append("=").append(val).append("&");
            }

        }
        request.setAttribute("pageLocation", request.getRequestURI());
        request.setAttribute("searchParams", sb.toString());
        request.setAttribute("sortBy", "sort=" + sortName + "&order=" + order);
        request.setAttribute("withPageSize", "pageSize=" + pagzSize);

        return new PageRequest(pageNumber - 1, pagzSize, sort);

    }

}
