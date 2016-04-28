package com.galkonltd.jnotify;

public class JNotifyTest {

	public static void main(String[] args) {
		Notifier.push(new Notification("This is a default messageeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.").autoClose(3000));
		Notifier.push(new Notification(NotifyType.INFO, "This is an information message.").autoClose(4000));
		Notifier.push(new Notification(NotifyType.SUCCESS, "This is a message indicating success.").autoClose(5000));
		Notifier.push(new Notification(NotifyType.WARNING, "This is a warning message.").autoClose(6000));
		Notifier.push(new Notification(NotifyType.ERROR, "This is an error message.").autoClose(7000));
		Notifier.push(new Notification(NotifyType.LINK, "Click to open http://www.google.com").autoClose(8000).callback(notification -> System.out.println("open google.com???")));
		Notifier.push(new Notification(NotifyType.MESSAGE, "This is a chat message notification.").autoClose(9000));
		new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (Notifier.isEmpty()) {
                        System.exit(0);
                    }
                }
        }).start();
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Notifier.push(new Notification(NotifyType.MESSAGE, "This is a chat message notification.").autoClose(5000));
		}
	}

}
