import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/add_application/add_application_bloc.dart';
import 'package:deeplom/screens/add_application/add_application_events.dart';
import 'package:deeplom/screens/add_application/add_application_screen.dart';
import 'package:deeplom/screens/add_application/add_application_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:get_it/get_it.dart';

class ApplicationScreen extends StatefulWidget {
  const ApplicationScreen({super.key});

  @override
  State<ApplicationScreen> createState() => _ApplicationScreenState();
}

class _ApplicationScreenState extends State<ApplicationScreen> {
  final _addApplicationBloc = AddApplicationBloc(
    const AddApplicationState(),
    mainRepository: GetIt.I<AbstractMainRepository>(),
  );

  @override
  void initState() {
    _addApplicationBloc.add(const OnInitSelectedApplication());
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
            if (state.selectedApplication.isLoading) {
              return const Center(
                child: CircularProgressIndicator(),
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
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      const SizedBox(height: 24.0),
                      Text(state.selectedApplication.requiredContent.title),
                      const SizedBox(height: 24.0),
                      Expanded(
                        child: ListView.separated(
                            itemBuilder: (context, index) {
                              var document = state.selectedApplication.requiredContent.documents[index];
                              var application = state.selectedApplication.requiredContent;
                              return Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Text(document.title),
                                  GestureDetector(
                                    onTap: () => showFileSourceDialog(context, application.id, application.title),
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
                            itemCount: state.selectedApplication.requiredContent.documents.length),
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

  void showFileSourceDialog(BuildContext context, String applicationId, String title) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text('Выберите источник файла'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: <Widget>[
              ListTile(
                leading: const Icon(Icons.photo_library),
                title: const Text('Выбрать из галереи'),
                onTap: () {
                  Navigator.of(context).pop();
                  _addApplicationBloc.add(PickFileEvent(FileSourceType.gallery, applicationId, title));
                },
              ),
              ListTile(
                leading: const Icon(Icons.camera_alt),
                title: const Text('Сделать снимок'),
                onTap: () {
                  Navigator.of(context).pop();
                  _addApplicationBloc.add(PickFileEvent(FileSourceType.camera, applicationId, title));
                },
              ),
              ListTile(
                leading: const Icon(Icons.attach_file),
                title: const Text('Выбрать с устройства'),
                onTap: () {
                  Navigator.of(context).pop();
                  _addApplicationBloc.add(PickFileEvent(FileSourceType.device, applicationId, title));
                },
              ),
            ],
          ),
        );
      },
    );
  }
}
