package data.sport.play;

public abstract class ActionResult {
	
	private String name ; 
	private int actionTime ; 
	private OffensiveAction offensiveAction ; 
	
	public ActionResult(String name) {
		this.name = name ; 
		actionTime = 0 ; 
		offensiveAction = null ; 
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name ; 
	}
	
	public void setActionTime(int actionTime) {
		this.actionTime = actionTime ; 
	}

	public int getActionTime() {
		return actionTime;
	}

	public OffensiveAction getOffensiveAction() {
		return offensiveAction;
	}

	public void setOffensiveAction(OffensiveAction offensiveAction) {
		this.offensiveAction = offensiveAction;
	}
	
	
	
	
	
	
}
