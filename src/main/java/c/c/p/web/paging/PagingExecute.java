/**
 * 
 */
package c.c.p.web.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author Administrator
 *
 */
public interface PagingExecute<T> {
	public Page<T> execute(PageRequest pageRequest) ;
}
