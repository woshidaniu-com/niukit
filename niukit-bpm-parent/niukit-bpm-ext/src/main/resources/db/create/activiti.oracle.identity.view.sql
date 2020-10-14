create or replace view ACT_ID_GROUP as
  select jsdm as ID_, 1 as REV_, jsmc as NAME_, 'assignment' as TYPE_ from niutal_xtgl_jsxxb where jsdm <> 'admin'
with READ ONLY;

create or replace view ACT_ID_USER as
  select zgh as ID_, 1 as REV_, xm as FIRST_, null as LAST_, dzyx as EMAIL_, null as PWD_, null as PICTURE_ID_ from niutal_xtgl_yhb where zgh <> 'admin'
with READ ONLY;

create or replace view ACT_ID_MEMBERSHIP as
  select jsdm as GROUP_ID_, zgh as USER_ID_ from niutal_xtgl_yhjsb where zgh <> 'admin'
with READ ONLY;


CREATE OR REPLACE VIEW V_ASSIGNED_TASKLIST AS
select distinct 
       A.ID_ AS P_TASK_ID,
       A.PROC_INST_ID_ P_PROC_INST_ID,
       A.TASK_DEF_KEY_ AS P_ACT_ID,
       A.NAME_ AS P_ACT_NAME,
			 A.OWNER_ AS P_OWNER,
       A.ASSIGNEE_ AS P_ASSIGNEE,
       A.DELEGATION_ AS P_DELEGATION,
       A.DESCRIPTION_ AS P_DESCRIPTION,
       TO_CHAR(A.CREATE_TIME_, 'YYYY-MM-DD HH24:MI:SS') AS P_CREATE_TIME,
       TO_CHAR(A.DUE_DATE_,'YYYY-MM-DD HH24:MI:SS') AS P_DUE_DATE
  from ACT_RU_TASK A
 inner join ACT_RE_PROCDEF D
    on A.PROC_DEF_ID_ = D.ID_
 WHERE A.ASSIGNEE_ IS NOT NULL
 order by A.ID_ asc;

CREATE or replace VIEW V_QUEUE_TASKLIST AS
SELECT A.ID_ AS P_TASK_ID,
       A.PROC_INST_ID_ AS P_PROC_INST_ID,
       A.TASK_DEF_KEY_ AS P_ACT_ID,
       A.NAME_ AS P_ACT_NAME,
			 A.OWNER_ AS P_OWNER,
       A.ASSIGNEE_ AS P_ASSIGNEE,
       A.DELEGATION_ AS P_DELEGATION,
       A.DESCRIPTION_ AS P_DESCRIPTION,
       TO_CHAR(A.CREATE_TIME_, 'YYYY-MM-DD HH24:MI:SS') AS P_CREATE_TIME,
       TO_CHAR(A.DUE_DATE_,'YYYY-MM-DD HH24:MI:SS') AS P_DUE_DATE,
       I.USER_ID P_CANDIDATE
  FROM ACT_RU_TASK A
  INNER JOIN (SELECT DISTINCT * FROM (SELECT TASK_ID_, TO_CHAR(USER_ID_) USER_ID
                    FROM ACT_RU_IDENTITYLINK I, ACT_RU_TASK T
                      WHERE TASK_ID_ IS NOT NULL
                        AND USER_ID_ IS NOT NULL
                        AND I.TASK_ID_ = T.ID_
                        AND T.ASSIGNEE_ IS NULL
                        AND TYPE_ = 'candidate'
                     UNION
                     SELECT TASK_ID_, R.USER_ID_
                       FROM ACT_RU_IDENTITYLINK I,act_id_membership R,ACT_RU_TASK T
                      WHERE I.TASK_ID_ IS NOT NULL
                        AND I.GROUP_ID_ IS NOT NULL
                        AND I.TASK_ID_ = T.ID_
                        AND T.ASSIGNEE_ IS NULL
                        AND TYPE_ = 'candidate'
                        AND I.GROUP_ID_ = R.GROUP_ID_)U) I
    ON A.ID_ = I.TASK_ID_;
    
CREATE or replace VIEW V_TASKLIST AS
SELECT A.ID_ AS P_TASK_ID,
       A.PROC_INST_ID_ AS P_PROC_INST_ID,
       A.TASK_DEF_KEY_ AS P_ACT_ID,
       A.NAME_ AS P_ACT_NAME,
			 A.OWNER_ AS P_OWNER,
       A.ASSIGNEE_ AS P_ASSIGNEE,
       A.DELEGATION_ AS P_DELEGATION,
       A.DESCRIPTION_ AS P_DESCRIPTION,
       TO_CHAR(A.CREATE_TIME_, 'YYYY-MM-DD HH24:MI:SS') AS P_CREATE_TIME,
       TO_CHAR(A.DUE_DATE_,'YYYY-MM-DD HH24:MI:SS') AS P_DUE_DATE,
       I.USER_ID P_CANDIDATE
  FROM ACT_RU_TASK A
  INNER JOIN (SELECT DISTINCT * FROM (SELECT TASK_ID_, TO_CHAR(USER_ID_) USER_ID
                    FROM ACT_RU_IDENTITYLINK I, ACT_RU_TASK T
                      WHERE TASK_ID_ IS NOT NULL
                        AND USER_ID_ IS NOT NULL
                        AND I.TASK_ID_ = T.ID_
                        AND T.ASSIGNEE_ IS NULL
                        AND TYPE_ = 'candidate'
                     UNION
                     SELECT TASK_ID_, R.USER_ID_
                       FROM ACT_RU_IDENTITYLINK I,act_id_membership R,ACT_RU_TASK T
                      WHERE I.TASK_ID_ IS NOT NULL
                        AND I.GROUP_ID_ IS NOT NULL
                        AND I.TASK_ID_ = T.ID_
                        AND T.ASSIGNEE_ IS NULL
                        AND TYPE_ = 'candidate'
                        AND I.GROUP_ID_ = R.GROUP_ID_
						UNION
						SELECT 
						 T.ID_  TASK_ID_,
						 TO_CHAR(T.ASSIGNEE_) USER_ID
				     FROM ACT_RU_TASK T
			       WHERE T.ASSIGNEE_ IS NOT NULL
						)U) I
    ON A.ID_ = I.TASK_ID_;
 

CREATE or replace VIEW V_HIS_TASKLIST AS
  SELECT a.proc_inst_id_ P_PROC_INST_ID,
				 a.proc_def_id_ P_PROC_DEF_ID,
				 a.task_def_key_ P_TASK_DEF_ID,
				 a.id_ P_TASK_ID,
				 a.name_ P_TASK_NAME,
				 a.assignee_ P_ASSIGNEE,
				 a.owner_ P_OWNER,
				 a.description_ P_DESCRIPTION,
				 a.category_ P_TASK_CATEGORY,
				 a.duration_ P_DURATION,
				 TO_CHAR(A.CLAIM_TIME_,'YYYY-MM-DD HH24:MI:SS') AS P_CLAIM_DATE,
				 TO_CHAR(A.START_TIME_, 'YYYY-MM-DD HH24:MI:SS') AS P_START_TIME,
				 TO_CHAR(A.END_TIME_,'YYYY-MM-DD HH24:MI:SS') AS P_END_TIME,
				 b.message_ as P_TASK_MESSAGE,
				 b.full_msg_ as P_TASK_FULL_MESSAGE
  FROM (SELECT row_number() OVER(PARTITION BY proc_inst_id_, assignee_ ORDER BY end_time_ DESC) LEV, t.*
          FROM act_hi_taskinst t
         WHERE t.assignee_ IS NOT NULL and t.end_time_ IS NOT NULL) a 
				 LEFT JOIN act_hi_comment b ON a.proc_inst_id_=b.proc_inst_id_ and a.id_ = b.task_id_ and b.type_='process_log'
 WHERE LEV = 1 ORDER BY a.END_TIME_;

 
--updated 2016/12/29
CREATE OR REPLACE VIEW V_PROCESS_TRACE AS
select "P_ACT_ID","P_ACT_NAME","P_ACT_TYPE","P_ASSIGNEE","P_USER_ID","P_DURATION","P_END_TIME","P_START_TIME","P_PROC_DEF_ID","P_PROC_INST_ID","P_TASK_ID","P_TASK_COMMENT","P_TASK_FULL_COMMENT" from (SELECT a.act_id_ as p_act_id,
       a.act_name_ as p_act_name,
       a.act_type_ as p_act_type,
       a.assignee_ as p_assignee,
       a.duration_ as p_duration,
       TO_CHAR(a.end_time_, 'YYYY-MM-DD HH24:MI:SS')as p_end_time,
       TO_CHAR(a.start_time_, 'YYYY-MM-DD HH24:MI:SS')as p_start_time,
       a.proc_def_id_ as p_proc_def_id,
       a.proc_inst_id_ as p_proc_inst_id,
       a.task_id_ as p_task_id,
			 b.user_id_ as p_user_id,
       b.message_ as p_task_comment,
       b.full_msg_ as P_TASK_FULL_COMMENT
  FROM act_hi_actinst a left join act_hi_comment b on a.task_id_ = b.task_id_
 where a.end_time_ is not null
   and a.act_type_ in ('startEvent', 'userTask', 'endEvent'))order by p_end_time asc;
   