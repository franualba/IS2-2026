import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "*",
  authDomain: "*.firebaseapp.com",
  projectId: "",
  storageBucket: "*.appspot.com",
  messagingSenderId: "*",
  appId: "*"
};

const app = initializeApp(firebaseConfig);
export const db = getFirestore(app);
