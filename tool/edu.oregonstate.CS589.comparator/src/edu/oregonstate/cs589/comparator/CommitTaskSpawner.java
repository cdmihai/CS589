package edu.oregonstate.cs589.comparator;

import java.io.IOException;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class CommitTaskSpawner implements ViewSpawner {

	private Task task;

	public CommitTaskSpawner(Task task) {
		this.task = task;
	}

	@Override
	public ManagedView spawnView() throws IOException, PartInitException {
		String viewId = CommitView.ID;
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IViewPart view = page.showView(viewId);
		CommitViewSetter viewSetter = (CommitViewSetter) view;
		viewSetter.setTask(task);

		return (ManagedView) view;
	}
}
