package iace.entity.sysAuth.sysApplication;

import java.util.HashSet;
import java.util.Set;

import iace.entity.sysAuth.sysOperation.SysOp;
import iace.entity.sysAuth.sysOperation.SysOpUpdate;
import iace.entity.sysAuth.sysOperation.SysOpView;

public class SysAppPopularActivity extends SysApp{
	private SysOpUpdate opUpdate = new SysOpUpdate();
	private SysOpView opView = new SysOpView();
	
	public SysAppPopularActivity(){
		super.displayName="hot20";
		super.namespaces.add("/popularActivity");
	}
	
	@Override
	public Set<SysOp> getOperationSet() {
		Set<SysOp> operations = new HashSet<SysOp>();
		operations.add(this.opView);
		operations.add(this.opUpdate);
		return operations;
	}

	public SysOpUpdate getOpUpdate() {
		return opUpdate;
	}

	public SysOpView getOpView() {
		return opView;
	}

}
