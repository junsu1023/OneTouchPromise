import {onDocumentCreated} from "firebase-functions/v2/firestore";
import * as admin from "firebase-admin";
import {getFirestore} from "firebase-admin/firestore";
import {getMessaging} from "firebase-admin/messaging";

admin.initializeApp();

export const sendNotificationOnMeetingCreated = onDocumentCreated(
  "meetings/{meetingId}",
  async (event) => {
    const snap = event.data;
    if (!snap) {
      console.log("No data found in snapshot.");
      return;
    }

    const meeting = snap.data();
    if (!meeting) {
      console.log("Meeting data is undefined.");
      return;
    }

    const userEmailList: string[] = meeting.participants ?? [];

    const db = getFirestore();

    const tokens: string[] = [];

    for (const email of userEmailList) {
      const userDoc = await db.collection("users").doc(email).get();
      const userData = userDoc.data();
      if (userData?.fcmToken) {
        tokens.push(userData.fcmToken);
      }
    }

    if (tokens.length === 0) {
      console.log("No tokens found.");
      return;
    }

    await getMessaging().sendEachForMulticast({
      tokens,
      notification: {
        title: "새로운 모임이 생성되었어요!",
        body: `${meeting.title} 모임에 참여해보세요!`,
      },
      android: {
        notification: {
          clickAction: "OPEN_MEETING_HOME",
        },
      },
    });
  }
);
