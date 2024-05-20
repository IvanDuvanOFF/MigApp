import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/add_application/add_application_bloc.dart';
import 'package:deeplom/screens/add_application/add_application_events.dart';
import 'package:deeplom/screens/add_application/add_application_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

enum FileSourceType {
  gallery,
  camera,
  device,
}

class AddApplicationScreen extends StatefulWidget {
  const AddApplicationScreen({super.key});

  @override
  State<AddApplicationScreen> createState() => _AddApplicationScreenState();
}

class _AddApplicationScreenState extends State<AddApplicationScreen> {
  final _addApplicationBloc = AddApplicationBloc(
    const AddApplicationState(),
    mainRepository: GetIt.I<AbstractMainRepository>(),
  );

  @override
  void initState() {
    _addApplicationBloc.add(const OnInit());
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        iconTheme: const IconThemeData(
          color: Colors.black,
        ),
      ),
      backgroundColor: Colors.white,
      body: SafeArea(
        child: BlocBuilder<AddApplicationBloc, AddApplicationState>(
          bloc: _addApplicationBloc,
          builder: (context, state) {
            if (state.applications.isLoading) {
              return const Center(
                child: CircularProgressIndicator(),
              );
            }
            if (state.applications.content == null) {
              return const Center(
                  child: Padding(
                padding: EdgeInsets.symmetric(horizontal: 32.0),
                child: Text(
                  'На данный момент доступные заявления отсутствуют',
                  style: TextStyle(fontSize: 16.0),
                  textAlign: TextAlign.center,
                ),
              ));
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
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      const SizedBox(height: 24.0),
                      const Text('Класс! Скоро здесь будут все документы...'),
                      const SizedBox(height: 24.0),
                      Expanded(
                        child: ListView.separated(
                            itemBuilder: (context, index) {
                              var application = state.applications.requiredContent[index];
                              return Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(application.title),
                                  GestureDetector(
                                    onTap: () => AppRouting.toApplication(),
                                    // onTap: () => showFileSourceDialog(context, application.id, application.title),
                                    child: Container(
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
                                      child: const Padding(
                                        padding: EdgeInsets.all(8.0),
                                        child: Icon(Icons.arrow_forward_ios),
                                      ),
                                    ),
                                  ),
                                ],
                              );
                            },
                            separatorBuilder: (context, index) {
                              return const SizedBox(height: 24.0);
                            },
                            itemCount: state.applications.requiredContent.length),
                      ),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
