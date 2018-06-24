package org.lots.candy.domain;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lots.candy.entity.Task;

@Mapper
public interface TaskMapper {

	@Select("select t.taskId, name , eachPoint, pointLimit, dailyPointLimit, earnedPoint, instruction, ifclosed, checkmethod "
			+ "from task t left join user_task a on a.taskId = t.taskId and a.userId =#{userId} order by rank")
	public List<Task> queryUserTask(String userId);
	
	@Update("replace into user_task (userId, taskId, earnedPoint) select #{userId},  taskId, eachPoint from task where taskId = #{taskId}")
	public void insertOnceTask(@Param("userId")String userId, @Param("taskId")String taskId);

}