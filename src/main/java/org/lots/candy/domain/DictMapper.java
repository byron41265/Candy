package org.lots.candy.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lots.candy.entity.Dict;

@Mapper
public interface DictMapper {
	
	@Select("SELECT dicId,    dicTypeId,    value,   `desc` FROM dic_item where dicTypeId = #{typeId} ")
	public List<Dict> queryByType(String typeId);

}
