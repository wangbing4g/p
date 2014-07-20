/**
 * 
 */
package c.c.p.web.paging;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import c.c.p.utils.Constants;

/**
 * @author Administrator
 *
 */
public class SpringPagingUtils {

    public static <T> void paging(HttpServletRequest request, PagingExecute<T> execute) {
        PageRequest pageRequest = createPageRequest(request);
        request.setAttribute("page", execute.execute(pageRequest));
    }

    public static <T> Page<T> pagingData(HttpServletRequest request, PagingExecute<T> execute) {
        PageRequest pageRequest = createPageRequest(request);
        return execute.execute(pageRequest);
    }

    protected static PageRequest createPageRequest(HttpServletRequest request) {

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
            } else if (Constants.PARAMETE_SORT.equals(key)) {
                sortName = map.get(key)[0];
            } else if (Constants.PARAMETE_ORDER.equals(key)) {
                order = map.get(key)[0];
            } else if (Constants.PARAMETE_PAGESIZE.equals(key)) {
                pagzSize = Integer.parseInt(map.get(key)[0]);
            } else if(Constants.PARAMETE_SELECTED_IDS.equals(key)){
                request.setAttribute(Constants.PARAMETE_SELECTED_IDS, map.get(key)[0]);
            }  else if(Constants.PARAMETE_PAGE_CONTAINER.equals(key)){
                request.setAttribute(Constants.PARAMETE_PAGE_CONTAINER, map.get(key)[0]);
            } else {
                params.put(key, map.get(key));
            }
        }
        if (StringUtils.hasText(sortName) && StringUtils.hasText(order)) {
            sort = new Sort(Direction.fromString(order), sortName);
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
