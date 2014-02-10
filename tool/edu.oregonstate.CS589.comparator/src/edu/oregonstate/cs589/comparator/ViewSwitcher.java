package edu.oregonstate.cs589.comparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import edu.oregonstate.cs589.comparator.commitview.CommitTaskSpawner;
import edu.oregonstate.cs589.comparator.infoview.InfoViewSpawner;

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
		
		viewSpawners.add(new InfoViewSpawner("PLEASE WAIT until prompted to start demo.", "Start Demo"));
		
		viewSpawners.add(new InfoViewSpawner("PLEASE WAIT until prompted to start practice task.", "Start Practice Task"));
		
		viewSpawners.add(new InfoViewSpawner("PLEASE WAIT until prompted to start the study tasks.", "Start Study"));
		
		addTasks();

		viewSpawnerIterator = viewSpawners.iterator();

		spawnNextView();

		return Status.OK_STATUS;
	}

	private void addTasks() {
		List<Task> tasks = DataProvider.getInstance().getTasks();

		for (Task task : tasks) {
			viewSpawners.add(new CommitTaskSpawner(task));
		}
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
