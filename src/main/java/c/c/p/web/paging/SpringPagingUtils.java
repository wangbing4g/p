/**
 * 
 */
package c.c.p.web.paging;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 *
 */
public class SpringPagingUtils {

    public static <T> void paging(ServletRequest request, PagingExecute<T> execute) {
        PageRequest pageRequest = createPageRequest(request);
        request.setAttribute("page", execute.execute(pageRequest));
    }

    public static <T> Page<T> pagingData(ServletRequest request, PagingExecute<T> execute) {
        PageRequest pageRequest = createPageRequest(request);
        return execute.execute(pageRequest);
    }

    protected static PageRequest createPageRequest(ServletRequest request) {

        Sort sort = null;

        int pageNumber = 1;
        int pagzSize = 10;
        String sortName = "";
        String order = "";
        Map<String, String[]> params = new HashMap<String, String[]>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            if (key.equals("page")) {
                pageNumber = Integer.parseInt(map.get(key)[0]);
            } else if ("sort".equals(key)) {
                sortName = map.get(key)[0];
            } else if ("order".equals(key)) {
                order = map.get(key)[0];
            } else if ("pageSize".equals(key)) {
                pagzSize = Integer.parseInt(map.get(key)[0]);
            } else {
                params.put(key, map.get(key));
            }
        }
        if (StringUtils.hasText(sortName) && StringUtils.hasText(order)) {
            sort = new Sort(Direction.fromString(order), sortName);
        }

        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            for (String val : params.get(key)) {
                sb.append(key).append("=").append(val).append("&");
            }

        }

        request.setAttribute("searchParams", sb.toString());
        request.setAttribute("sortBy", "sort=" + sortName + "&order=" + order);

        return new PageRequest(pageNumber - 1, pagzSize, sort);

    }

}
