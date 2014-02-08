package edu.oregonstate.cs589.comparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

public class ViewSwitcher extends UIJob implements FinishCallback {

	private List<ViewSpawner> viewSpawners;
	private Iterator<ViewSpawner> viewSpawnerIterator;

	public ViewSwitcher(String string) {
		super(string);
		viewSpawners = new ArrayList<>();
	}

	@SuppressWarnings({ "unused", "restriction" })
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {

		List<Task> tasks = DataProvider.getInstance().getTasks();

		for (Task task : tasks) {
			viewSpawners.add(new CommitTaskSpawner(task));
		}

		viewSpawnerIterator = viewSpawners.iterator();

		spawnNextView();

		return Status.OK_STATUS;
	}

	private void spawnNextView() {
		try {

			if (viewSpawnerIterator.hasNext()) {
				ViewSpawner viewSpawner = viewSpawnerIterator.next();
				ManagedView view = viewSpawner.spawnView();
				view.addFinishCallback(this);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void iAmDone() {
		spawnNextView();
	}
}