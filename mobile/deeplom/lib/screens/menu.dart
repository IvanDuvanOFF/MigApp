import 'package:deeplom/screens/faq/faq_screen.dart';
import 'package:deeplom/screens/main/main_screen.dart';
import 'package:deeplom/screens/profile/profile_screen.dart';
import 'package:deeplom/screens/settings/settings_screen.dart';
import 'package:flutter/material.dart';

class MainMenu extends StatefulWidget {
  const MainMenu({super.key});

  @override
  State<MainMenu> createState() => _MainMenuState();
}

class _MainMenuState extends State<MainMenu> with TickerProviderStateMixin {
  int _selectedIndex = 0;

  void _onItemTapped(int index) {
    if (_selectedIndex != index) {
      setState(() {
        _selectedIndex = index;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: IndexedStack(
        index: _selectedIndex,
        children: const [
          MainScreen(),
          FaqScreen(),
          SettingsScreen(),
          ProfileScreen(),
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
        // backgroundColor: Theme.of(context).primaryColor,
        currentIndex: _selectedIndex,
        selectedItemColor: Colors.black,
        unselectedItemColor: Colors.black,
        type: BottomNavigationBarType.fixed,
        onTap: _onItemTapped,
        items: [
          BottomNavigationBarItem(
            icon: Container(
              decoration: _selectedIndex == 0 ? activeElement : const BoxDecoration(),
              child: const Padding(
                padding: EdgeInsets.symmetric(horizontal: 14.0, vertical: 6.0),
                child: Icon(Icons.home),
              ),
            ),
            label: '',
          ),
          BottomNavigationBarItem(
            icon: Container(
              decoration: _selectedIndex == 1 ? activeElement : const BoxDecoration(),
              child: const Padding(
                padding: EdgeInsets.symmetric(horizontal: 14.0, vertical: 6.0),
                child: Icon(Icons.question_answer_outlined),
              ),
            ),
            label: '',
          ),
          BottomNavigationBarItem(
            icon: Container(
              decoration: _selectedIndex == 2 ? activeElement : const BoxDecoration(),
              child: const Padding(
                padding: EdgeInsets.symmetric(horizontal: 14.0, vertical: 6.0),
                child: Icon(Icons.settings),
              ),
            ),
            label: '',
          ),
          BottomNavigationBarItem(
            icon: Container(
              decoration: _selectedIndex == 3 ? activeElement : const BoxDecoration(),
              child: const Padding(
                padding: EdgeInsets.symmetric(horizontal: 14.0, vertical: 6.0),
                child: Icon(Icons.person),
              ),
            ),
            label: '',
          ),
        ],
      ),
    );
  }
}

BoxDecoration activeElement = BoxDecoration(
  borderRadius: BorderRadius.circular(12.0),
  boxShadow: [
    BoxShadow(
      color: Colors.black.withOpacity(.5),
    ),
    const BoxShadow(
      color: Colors.grey,
      spreadRadius: -2.0,
      blurRadius: 2.0,
    ),
  ],
);
