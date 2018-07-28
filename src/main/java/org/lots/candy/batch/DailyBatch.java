package org.lots.candy.batch;

import org.lots.candy.domain.BatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyBatch {
	
	@Autowired
	private BatchMapper batchMapper;
	
	public final static long time = 3*60*60*1000;
	@Scheduled(fixedRate=time)
	public void startDailyBatch(){
		batchMapper.updateUserTaskTwitter();
		batchMapper.updateAction();
		batchMapper.updateUserTask();
		batchMapper.updateRank();
	}
}
