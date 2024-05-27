import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/notifications/notifications_bloc.dart';
import 'package:deeplom/screens/notifications/notifications_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

import 'notifications_events.dart';

class NotificationsScreen extends StatefulWidget {
  const NotificationsScreen({super.key});

  @override
  State<NotificationsScreen> createState() => _NotificationsScreenState();
}

class _NotificationsScreenState extends State<NotificationsScreen> {
  final _notificationBloc = NotificationsBloc(
    const NotificationsState(),
    mainRepository: GetIt.I<AbstractMainRepository>(),
  );

  @override
  void initState() {
    _notificationBloc.add(OnInit());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Уведомления',
          style: TextStyle(color: Colors.black),
        ),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
        iconTheme: const IconThemeData(
          color: Colors.black,
        ),
      ),
      backgroundColor: Colors.white,
      body: SafeArea(
        child: BlocBuilder<NotificationsBloc, NotificationsState>(
            bloc: _notificationBloc,
            builder: (context, state) {
              if (state.notifications.isLoading) {
                return const Center(
                  child: CircularProgressIndicator(),
                );
              }
              if (state.notifications.requiredContent.isEmpty) {
                return const Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.notifications_off,
                        size: 64.0,
                      ),
                      Text('Уведомлений нет'),
                    ],
                  ),
                );
              }
              return Padding(
                padding: const EdgeInsets.all(18.0),
                child: Container(
                  height: MediaQuery.of(context).size.height / 1.5,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(8.0),
                    boxShadow: const [
                      BoxShadow(
                        color: Colors.black12,
                        blurRadius: 10.0,
                        spreadRadius: 1.0,
                      ),
                    ],
                  ),
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 18.0),
                    child: ListView.separated(
                        itemBuilder: (context, index) {
                          var notification = state.notifications.requiredContent[index];
                          return Theme(
                            data: Theme.of(context).copyWith(
                              dividerColor: Colors.transparent,
                            ),
                            child: ExpansionTile(
                              leading: const Icon(Icons.mail),
                              title: Text(notification.title),
                              expandedAlignment: Alignment.centerLeft,
                              children: [
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(notification.description),
                                    IconButton(
                                      onPressed: () => _notificationBloc.add(DeleteNotification(notificationId: notification.notificationId)),
                                      icon: const Icon(Icons.delete),
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          );
                        },
                        separatorBuilder: (context, index) {
                          return const SizedBox(height: 12.0);
                        },
                        itemCount: state.notifications.requiredContent.length),
                  ),
                ),
              );
            }),
      ),
    );
  }
}
