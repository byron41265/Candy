package org.lots.candy.domain;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lots.candy.entity.Task;

@Mapper
public interface TaskMapper {

	@Select("select t.taskId, name , eachPoint, pointLimit, dailyPointLimit, earnedPoint, instruction, ifclosed, checkmethod "
			+ "from task t left join user_task a on a.taskId = t.taskId and a.userId =#{userId}")
	public List<Task> queryUserTask(String userId);

}
