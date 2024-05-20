import 'package:deeplom/screens/add_application/add_application_screen.dart';
import 'package:deeplom/screens/add_application/selected_application/application_screen.dart';
import 'package:deeplom/screens/auth/auth_screen.dart';
import 'package:deeplom/screens/auth/reset_password.dart';
import 'package:deeplom/screens/faq/faq_screen.dart';
import 'package:deeplom/screens/main/main_screen.dart';
import 'package:deeplom/screens/menu.dart';
import 'package:deeplom/screens/notifications/notifications_screen.dart';
import 'package:deeplom/screens/profile/profile_screen.dart';
import 'package:deeplom/screens/settings/settings_screen.dart';
import 'package:deeplom/screens/splash_screen.dart';
import 'package:go_router/go_router.dart';

final appRouter = GoRouter(
  initialLocation: '/splash',
  routes: [
    GoRoute(
      name: 'splash',
      path: '/splash',
      builder: (context, state) => const SplashScreen(),
    ),
    GoRoute(
      name: 'menu',
      path: '/menu',
      builder: (context, state) => const MainMenu(),
    ),
    GoRoute(
      name: 'auth',
      path: '/auth',
      builder: (context, state) => const AuthScreen(),
    ),
    GoRoute(
      name: 'main',
      path: '/main',
      builder: (context, state) => const MainScreen(),
    ),
    GoRoute(
      name: 'profile',
      path: '/profile',
      builder: (context, state) => const ProfileScreen(),
    ),
    GoRoute(
      name: 'addApplication',
      path: '/addApplication',
      builder: (context, state) => const AddApplicationScreen(),
    ),
    GoRoute(
      name: 'application',
      path: '/application',
      builder: (context, state) => const ApplicationScreen(),
    ),
    GoRoute(
      name: 'faq',
      path: '/faq',
      builder: (context, state) => const FaqScreen(),
    ),
    GoRoute(
      name: 'settings',
      path: '/settings',
      builder: (context, state) => const SettingsScreen(),
    ),
    GoRoute(
      name: 'resetPass',
      path: '/resetPass',
      builder: (context, state) => const ResetPassword(),
    ),
    GoRoute(
      name: 'notifications',
      path: '/notifications',
      builder: (context, state) => const NotificationsScreen(),
    ),
  ],
);

class AppRouting {
  AppRouting._();
  static void toMenu() => appRouter.pushReplacement('/menu');
  static void toAuth() => appRouter.pushReplacement('/auth');
  static void toMain() => appRouter.push('/main');
  static void toProfile() => appRouter.push('/profile');
  static void toAddApplication() => appRouter.push('/addApplication');
  static void toFaq() => appRouter.push('/faq');
  static void toSettings() => appRouter.push('/settings');
  static void toResetPassword() => appRouter.push('/resetPass');
  static void toNotifications() => appRouter.push('/notifications');
  static void toApplication() => appRouter.push('/application');
}
