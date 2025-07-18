package study.common.util;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonMapper {

    CommonMap selectOne();
    CommonMap selectOne(Object params);
    List<CommonMap> selectList();
    List<CommonMap> selectList(Object params);
    int insert(Object params);
    int update(Object params);
    int delete(Object params);

}
