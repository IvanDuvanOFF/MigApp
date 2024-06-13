import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:deeplom/screens/main/main_bloc.dart';
import 'package:deeplom/screens/main/main_events.dart';
import 'package:deeplom/screens/main/main_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';
import 'package:intl/intl.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  final _mainBloc = MainBloc(const MainState(), mainRepository: GetIt.I<AbstractMainRepository>());

  @override
  void initState() {
    _mainBloc.add(OnInit());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      extendBodyBehindAppBar: true,
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(right: 12.0, bottom: 24.0),
        child: FloatingActionButton(
          backgroundColor: Colors.grey.withOpacity(.7),
          elevation: 0,
          highlightElevation: 0,
          onPressed: () => AppRouting.toAddApplication(),
          child: Icon(
            Icons.add,
            color: Colors.black.withOpacity(.7),
          ),
        ),
      ),
      // floatingActionButtonLocation: FloatingActionButtonLocation.startTop,
      body: SafeArea(
        child: BlocBuilder<MainBloc, MainState>(
            bloc: _mainBloc,
            builder: (context, state) {
              if (state.mainData.isLoading && state.applications.isLoading && state.notifications.isLoading) {
                return const Center(child: CircularProgressIndicator());
              }
              bool allNotificationViewed =
                  state.notifications.requiredContent.isEmpty || state.notifications.requiredContent.every((notification) => notification.isViewed);

              return Padding(
                padding: const EdgeInsets.only(top: 16.0),
                child: SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 18.0),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Padding(
                              padding: const EdgeInsets.all(24.0),
                              child: Image.asset(
                                'assets/logo.jpeg',
                                scale: 3,
                              ),
                            ),
                            InkWell(
                              onTap: () => AppRouting.toNotifications(),
                              child: Container(
                                decoration: BoxDecoration(color: Colors.grey.withOpacity(0.5), borderRadius: BorderRadius.circular(32.0)),
                                child: Padding(
                                  padding: const EdgeInsets.all(12.0),
                                  child: Icon(
                                    Icons.notifications_none,
                                    color: allNotificationViewed ? Colors.black : Colors.red,
                                  ),
                                ),
                              ),
                            )
                          ],
                        ),
                      ),
                      // const SizedBox(height: 48.0),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 18.0),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisSize: MainAxisSize.max,
                              children: [
                                Text(
                                  '${state.mainData.requiredContent.surname} ${state.mainData.requiredContent.name} ${state.mainData.requiredContent.patronymic}',
                                  style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w500),
                                ),
                                const SizedBox(height: 12.0),
                                Text(
                                    '${state.mainData.requiredContent.institute.isEmpty ? '-' : state.mainData.requiredContent.institute}, ${state.mainData.requiredContent.group.isEmpty ? '-' : state.mainData.requiredContent.group}'),
                              ],
                            ),
                            Container(
                              height: 128.0,
                              width: 128.0,
                              decoration: state.mainData.requiredContent.photo.isNotEmpty
                                  ? BoxDecoration(
                                      image: DecorationImage(image: MemoryImage(state.avatar!), fit: BoxFit.cover),
                                      color: Colors.grey.withOpacity(0.5),
                                      borderRadius: BorderRadius.circular(64.0),
                                    )
                                  : BoxDecoration(
                                      color: Colors.grey.withOpacity(0.5),
                                      borderRadius: BorderRadius.circular(64.0),
                                    ),
                              child: state.mainData.requiredContent.photo.isNotEmpty
                                  ? const SizedBox()
                                  : const Center(
                                      child: Icon(
                                      Icons.person,
                                      size: 64.0,
                                    )),
                            )
                          ],
                        ),
                      ),
                      const SizedBox(height: 24.0),
                      DateList(
                        dateList: state.currentMonthDates,
                        mainBloc: _mainBloc,
                        allApplicationVisible: state.allApplicationShow,
                      ),
                      const SizedBox(height: 24.0),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 18.0),
                        child: Container(
                          width: double.infinity,
                          height: MediaQuery.of(context).size.height / 2.2,
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(32.0),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withOpacity(0.3),
                                blurRadius: 2.0,
                                offset: const Offset(1, 1),
                              ),
                            ],
                          ),
                          child: Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 18.0, vertical: 12.0),
                            child: Column(
                              mainAxisSize: MainAxisSize.max,
                              children: [
                                Text(
                                  S.of(context).applicationsText,
                                  style: const TextStyle(fontSize: 16.0),
                                ),
                                TextButton(
                                  onPressed: () => _mainBloc.add(GetAllApplication()),
                                  child: Text(S.of(context).showAllApplication),
                                ),
                                !state.applications.requiredContent.isNotEmpty
                                    ? Padding(
                                        padding: const EdgeInsets.only(top: 32.0),
                                        child: Text(
                                          S.of(context).applicationDisplayedHereText,
                                          textAlign: TextAlign.center,
                                        ),
                                      )
                                    : Expanded(
                                        child: ListView.separated(
                                          shrinkWrap: true,
                                          itemBuilder: (context, index) {
                                            return InkWell(
                                              onTap: () => AppRouting.toAddApplication(),
                                              child: Row(
                                                children: [
                                                  applicationIcon(state.applications.requiredContent[index].status),
                                                  Expanded(
                                                    child: Text(
                                                      state.applications.content?[index].title ?? '',
                                                      overflow: TextOverflow.ellipsis,
                                                    ),
                                                  ),
                                                  const Icon(Icons.arrow_forward_ios),
                                                ],
                                              ),
                                            );
                                          },
                                          separatorBuilder: (context, index) {
                                            return const Divider();
                                          },
                                          itemCount: state.applications.content?.length ?? 0,
                                        ),
                                      ),
                              ],
                            ),
                          ),
                        ),
                      )
                    ],
                  ),
                ),
              );
            }),
      ),
    );
  }
}

Icon applicationIcon(ApplicationStatus status) {
  switch (status) {
    case ApplicationStatus.done:
      return const Icon(Icons.done);

    case ApplicationStatus.loading:
      return const Icon(Icons.access_time_outlined);

    case ApplicationStatus.failed:
      return const Icon(Icons.error);

    case ApplicationStatus.in_progress:
      return const Icon(Icons.update);

    case ApplicationStatus.saved:
      return const Icon(Icons.save_alt);
  }
}

class DateList extends StatefulWidget {
  const DateList({
    super.key,
    required this.dateList,
    required this.mainBloc,
    required this.allApplicationVisible,
  });

  final MainBloc mainBloc;
  final List<DateTime> dateList;
  final bool allApplicationVisible;

  @override
  State<DateList> createState() => _DateListState();
}

class _DateListState extends State<DateList> {
  final ScrollController _scrollController = ScrollController();
  DateTime? _selectedDate;

  @override
  void initState() {
    super.initState();

    int todayIndex = widget.dateList.indexWhere((date) => date.day == DateTime.now().day);
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (todayIndex != -1) {
        _scrollController.jumpTo(
          todayIndex * 60.0,
        );
      }
      setState(() {
        _selectedDate = widget.dateList[todayIndex];
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      height: 80.0,
      color: Colors.grey.withOpacity(.2),
      child: ListView.separated(
        controller: _scrollController,
        scrollDirection: Axis.horizontal,
        itemCount: widget.dateList.length,
        itemBuilder: (context, index) {
          DateTime date = widget.dateList[index];
          bool isSelected = _selectedDate?.day == date.day;

          return GestureDetector(
            onTap: () {
              setState(() {
                _selectedDate = date;
                widget.mainBloc.add(GetApplicationByDate(date: _selectedDate.toString()));
              });
            },
            child: Padding(
              padding: const EdgeInsets.symmetric(vertical: 8.0),
              child: Container(
                decoration: BoxDecoration(
                  color: isSelected && !widget.allApplicationVisible ? Colors.grey.shade900 : Colors.grey.withOpacity(0.5),
                  borderRadius: BorderRadius.circular(12.0),
                ),
                child: Padding(
                  padding: const EdgeInsets.symmetric(vertical: 4.0, horizontal: 16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        DateFormat('MMMM').format(date),
                        style: TextStyle(color: isSelected && !widget.allApplicationVisible ? Colors.white : Colors.black),
                      ),
                      Text(
                        '${date.day}',
                        style: TextStyle(color: isSelected && !widget.allApplicationVisible ? Colors.white : Colors.black),
                      ),
                      Text(
                        getDayOfWeek(date.weekday, context),
                        style: TextStyle(color: isSelected && !widget.allApplicationVisible ? Colors.white : Colors.black),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          );
        },
        separatorBuilder: (BuildContext context, int index) {
          return const SizedBox(width: 12.0);
        },
      ),
    );
  }
}

String getDayOfWeek(int day, context) {
  switch (day) {
    case 1:
      return S.of(context).monday;
    case 2:
      return S.of(context).tuesday;
    case 3:
      return S.of(context).wednesday;
    case 4:
      return S.of(context).thursday;
    case 5:
      return S.of(context).friday;
    case 6:
      return S.of(context).saturday;
    case 7:
      return S.of(context).sunday;
    default:
      throw ArgumentError('Invalid day: $day. Day must be in range 1-7.');
  }
}
