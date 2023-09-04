#ifndef MAIN_TASKS_COMMON_H_
#define MAIN_TASKS_COMMON_H_
/** Core 0
*/
//Comunication application task
#define COMUNICATION_TASK_STACK_SIZE			 4096
#define COMUNICATION_TASK_PRIORITY				    0
#define COMUNICATION_TASK_CORE_ID				    0

/** Core 1
*Priority task
*/
// Panel application task
#define PANEL_TASK_STACK_SIZE			 4096
#define PANEL_TASK_PRIORITY				    0
#define PANEL_TASK_CORE_ID				    1

// Control application task
#define CONTROL_TASK_STACK_SIZE			 4096
#define CONTROL_TASK_PRIORITY				1
#define CONTROL_TASK_CORE_ID				1

#endif
