import 'dart:io';
import 'package:deeplom/config/lce.dart';
import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/screens/add_application/add_application_events.dart';
import 'package:deeplom/screens/add_application/add_application_screen.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import 'package:image_picker/image_picker.dart';
import 'package:file_picker/file_picker.dart';
import 'add_application_state.dart';

class AddApplicationBloc extends Bloc<AddApplicationEvents, AddApplicationState> {
  final AbstractMainRepository mainRepository;
  final _secureStorage = const FlutterSecureStorage();

  AddApplicationBloc(super.initialState, {required this.mainRepository}) {
    on<OnInit>(_onInit);
    on<OnInitSelectedApplication>(_onInitSelectedApplication);
    on<PickFileEvent>(_onPickFile);
    on<UploadFileEvent>(_onUploadFile);
    on<AddDocumentEvent>(_onAddDocument);
    on<SelectApplication>(_onSelectApplication);
  }

  Future<void> _onInit(OnInit event, Emitter<AddApplicationState> emit) async {
    try {
      var applicationData = await mainRepository.getApplications();
      // List<ApplicationsModel> newApplications = applicationData.map((e) => e.title).toList();
      List<ApplicationsModel> newApplications = applicationData
          .fold<Map<String, ApplicationsModel>>({}, (map, item) {
            if (!map.containsKey(item.title)) {
              map[item.title] = item;
            }
            return map;
          })
          .values
          .toList();
      emit(state.copyWith(applications: applicationData.asContent, newApplications: newApplications.asContent));
    } catch (e) {
      emit(state.copyWith(applications: const Lce.idle()));
    }
    // List<ApplicationsModel> applicationData = [
    //   const ApplicationsModel(id: '1', title: 'Получение миграционного учета', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '2', title: 'Получение/продление ВИЗы', status: ApplicationStatus.in_progress),
    //   const ApplicationsModel(id: '3', title: 'Договор на проживание в общежитии', status: ApplicationStatus.in_progress),
    // ];
  }

  Future<void> _onInitSelectedApplication(OnInitSelectedApplication event, Emitter<AddApplicationState> emit) async {
    String? applicationId = await _secureStorage.read(key: 'applicationId');
    print('APPLICATION ID:: $applicationId');
    var application = await mainRepository.selectApplication(applicationId: applicationId ?? '');
    // const SelectedApplicationModel application = SelectedApplicationModel(
    //   id: 'application_123',
    //   title: 'Application for Migration Registration',
    //   status: 'Pending',
    //   documents: [
    //     DocumentModel(
    //       id: 'document_1',
    //       title: 'Паспорт',
    //       link: 'https://example.com/passport_1.pdf',
    //       status: 'Approved',
    //     ),
    //     DocumentModel(
    //       id: 'document_2',
    //       title: 'Паспорт',
    //       link: 'https://example.com/passport_2.pdf',
    //       status: 'Pending',
    //     ),
    //     DocumentModel(
    //       id: 'document_3',
    //       title: 'Виза',
    //       link: 'https://example.com/visa_1.pdf',
    //       status: 'Approved',
    //     ),
    //     DocumentModel(
    //       id: 'document_4',
    //       title: 'Виза',
    //       link: 'https://example.com/visa_2.pdf',
    //       status: 'Expired',
    //     ),
    //   ],
    // );

    emit(state.copyWith(selectedApplication: application.asContent));
  }

  Future<void> _onSelectApplication(SelectApplication event, Emitter<AddApplicationState> emit) async {
    await _secureStorage.write(key: 'applicationId', value: event.applicationId);
    AppRouting.toApplication(applicationId: event.applicationId);
  }

  Future<void> _onPickFile(PickFileEvent event, Emitter<AddApplicationState> emit) async {
    final ImagePicker picker = ImagePicker();
    XFile? xFile;
    File? file;

    if (event.sourceType == FileSourceType.gallery) {
      xFile = await picker.pickImage(source: ImageSource.gallery);
    } else if (event.sourceType == FileSourceType.camera) {
      xFile = await picker.pickImage(source: ImageSource.camera);
    } else if (event.sourceType == FileSourceType.device) {
      FilePickerResult? result = await FilePicker.platform.pickFiles();
      if (result != null && result.files.single.path != null) {
        file = File(result.files.single.path!);
      }
    }

    if (xFile != null) {
      file = File(xFile.path);
    }

    if (file != null) {
      add(UploadFileEvent(file, event.applicationId, event.applicationTitle));
    }
  }

  Future<void> _onUploadFile(UploadFileEvent event, Emitter<AddApplicationState> emit) async {
    try {
      final fileModel = await mainRepository.uploadFile(file: event.file);
      String fileName = fileModel?.name ?? '';
      print('FN:: $fileName');
      var document = await mainRepository.addDocument(applicationId: event.applicationId, fileName: fileName, title: event.applicationTitle);
      print('DOCUMENT BLOC TEST:: ${document?.status}');
    } catch (e) {
      print('Error: $e');
    }
  }

  Future<void> _onAddDocument(AddDocumentEvent event, Emitter<AddApplicationState> emit) async {
    try {
      // await mainRepository.addDocument(applicationId: event.applicationId, fileName: event.fileName);
    } catch (e) {
      print('Error: $e');
    }
  }
}
