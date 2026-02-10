import { createBrowserRouter } from "react-router";
import { Layout } from "./components/Layout";
import { MainScreen } from "./components/MainScreen";
import { NavigationScreen } from "./components/NavigationScreen";
import { WalkingScreen } from "./components/WalkingScreen";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: Layout,
    children: [
      { index: true, Component: MainScreen },
      { path: "navigation", Component: NavigationScreen },
      { path: "walking", Component: WalkingScreen },
    ],
  },
]);
