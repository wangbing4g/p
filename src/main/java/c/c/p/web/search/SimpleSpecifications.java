/**
 * 
 */
package c.c.p.web.search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import c.c.p.web.search.annotations.Equal;
import c.c.p.web.search.annotations.ForSearch;
import c.c.p.web.search.annotations.GreaterThanOrEqualTo;
import c.c.p.web.search.annotations.GreaterThen;
import c.c.p.web.search.annotations.LessThan;
import c.c.p.web.search.annotations.LessThanOrEqualTo;
import c.c.p.web.search.annotations.LikeAnyWhere;
import c.c.p.web.search.annotations.LikeLeft;
import c.c.p.web.search.annotations.LikeRight;

/**
 * @author Administrator
 *
 */
public class SimpleSpecifications {

    private static Logger LOG = LoggerFactory.getLogger(SimpleSpecifications.class);

    public static <T> Specification<T> bySearchDto(final Object searchDto, final Class<T> entityClazz) {

        LOG.debug("Get Specification from searchDto[{}]", searchDto);

        if (!searchDto.getClass().isAnnotationPresent(ForSearch.class)) {
            LOG.error("Ignore field[{}] with empty value");
        }

        return new Specification<T>() {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> plist = new ArrayList<Predicate>();

                for (Field field : searchDto.getClass().getDeclaredFields()) {

                    try {

                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }

                        Object value = field.get(searchDto);

                        if (value == null || StringUtils.isEmpty(value.toString())) {

                            LOG.debug("Ignore field[{}] with empty value", field.getName());
                            continue;
                        }

                        LOG.debug("Process field[{}] with value[{}]", field.getName(), value);

                        if (field.isAnnotationPresent(Equal.class)) {

                            String attributeName = field.getAnnotation(Equal.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.equal(p, value));

                        } else if (field.isAnnotationPresent(GreaterThanOrEqualTo.class)) {

                            String attributeName = field.getAnnotation(GreaterThanOrEqualTo.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.greaterThanOrEqualTo(p, (Comparable) value));
                        } else if (field.isAnnotationPresent(GreaterThen.class)) {
                            String attributeName = field.getAnnotation(GreaterThen.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.greaterThan(p, (Comparable) value));
                        } else if (field.isAnnotationPresent(LessThan.class)) {
                            String attributeName = field.getAnnotation(LessThan.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.lessThan(p, (Comparable) value));
                        } else if (field.isAnnotationPresent(LessThanOrEqualTo.class)) {
                            String attributeName = field.getAnnotation(LessThanOrEqualTo.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.lessThanOrEqualTo(p, (Comparable) value));
                        } else if (field.isAnnotationPresent(LikeAnyWhere.class)) {
                            String attributeName = field.getAnnotation(LikeAnyWhere.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.like(p, "%" + value + "%"));
                        } else if (field.isAnnotationPresent(LikeLeft.class)) {
                            String attributeName = field.getAnnotation(LikeLeft.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.like(p, "%" + value));
                        } else if (field.isAnnotationPresent(LikeRight.class)) {
                            String attributeName = field.getAnnotation(LikeRight.class).name();
                            if (StringUtils.isEmpty(attributeName)) {
                                attributeName = field.getName();
                            }

                            Path p = getPath(root, attributeName);

                            plist.add(cb.like(p, value + "%"));
                        } else {

                            String attributeName = field.getName();
                            Path p = getPath(root, attributeName);

                            if (value instanceof String) {
                                plist.add(cb.like(p, "%" + value + "%"));
                            } else {
                                plist.add(cb.equal(p, value));
                            }
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }

                if (plist.size() > 0) {
                    return cb.and(plist.toArray(new Predicate[plist.size()]));
                }

                return cb.conjunction();
            }

            @SuppressWarnings({ "hiding", "rawtypes" })
            private <T> Path getPath(Root<T> root, String attributeName) {

                Path p = root.get(attributeName);
                return p;
            }
        };
    }
}
