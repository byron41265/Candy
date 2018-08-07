package org.lots.candy.domain;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lots.candy.entity.Action;
import org.lots.candy.entity.Task;
import org.lots.candy.entity.TaskType;

import com.github.pagehelper.Page;

@Mapper
public interface TaskMapper {

	@Select("select t.taskId, name , eachPoint, eachPoint1,  pointLimit, dailyPointLimit, earnedPoint, instruction, ifclosed, checkmethod, typeId "
			+ "from task t left join user_task a on a.taskId = t.taskId and a.userId =#{userId} where ifEffective ='Y' and phase = 1 order by `rank`")
	public List<Task> queryUserTask(String userId);
	
	@Select("SELECT tt.`typeId`, tt.`fullname`, tt.`shortname` , IFNULL(sum(a.earnedPoint),0) earnedPoint "
			+ "FROM `task_type` tt left join  task t on tt.typeId  = t.typeId "
			+ "left join user_task a on a.taskId = t.taskId and a.userId =#{userId} "
			+ "group by tt.`typeId`, tt.`fullname`, tt.`shortname`")
	public List<TaskType> queryUserTaskType(@Param("userId")String userId );
	
	@Update("replace into user_task (userId, taskId, earnedPoint) select #{userId},  taskId, eachPoint from task where taskId = #{taskId}")
	public void insertOnceTask(@Param("userId")String userId, @Param("taskId")String taskId);
	
	@Update("replace into user_task (userId, taskId, earnedPoint) values(#{userId}, #{taskId}, #{earnedPoint})")
	public void insertTask(@Param("userId") String userId, @Param("taskId") String taskId, @Param("earnedPoint") int earnedPoint);
	
	@Insert("replace INTO `action`(`userId`,`taskId`,`submitUrl`,`if_effective`,`if_handled`) VALUES(#{userId},  #{taskId} , #{link} , 'N' , 'N')")
	public void insertLinkAction(@Param("userId")String userId, @Param("link")String link,@Param("taskId")String taskId);
	
	@Select("select eachPoint from task where taskId = '15'")
	public int getEachInvitePoint();
	
	@Select("<script>select a.`userId`, u.userName, a.`taskId`, t.`name` taskName, t.eachPoint taskEachPoint, t.eachPoint1 taskEachPoint1, a.`earned_point`, a.`submitUrl`, DATE_FORMAT(a.`submitTime`,'%Y-%m-%d %H:%i:%s') submitTime, a.`if_effective` ifEffective, a.`if_handled` ifHandled "
			+ "from `action` a join `user` u on a.userId = u.userId join task t on a.taskId = t.taskId "
			+ "where a.`if_handled` = #{ifHandled} "
			+ "<if test=\"userName!=null and userName!=''\"> and u.userName like '%'||#{userName}||'%'</if>"
			+ "<if test=\"taskId!=null and taskId!=''\"> and a.taskId = #{taskId}</if>"
			+ "order by a.`if_handled`,  a.`taskId`, a.`userId` , a.`submitTime` desc</script>")
	public Page<Action> queryActions(@Param("userName")String userName,@Param("taskId")String taskId, @Param("ifHandled")String ifHandled);
	
}
