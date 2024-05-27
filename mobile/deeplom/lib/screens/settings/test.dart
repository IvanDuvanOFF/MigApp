// import 'package:dio/dio.dart';
// import 'package:file_picker/file_picker.dart';
// import 'package:flutter/material.dart';
// import 'package:image_picker/image_picker.dart';
// import 'package:path_provider/path_provider.dart';
// import 'dart:convert';
// import 'dart:io';
//
// enum FileSourceType {
//   gallery,
//   camera,
//   device,
// }
//
// class UploadFileScreen extends StatefulWidget {
//   @override
//   _UploadFileScreenState createState() => _UploadFileScreenState();
// }
//
// class _UploadFileScreenState extends State<UploadFileScreen> {
//   final Dio dio = Dio();
//   final ImagePicker _picker = ImagePicker();
//   String? fileName;
//
//   Future<void> uploadFile(File file) async {
//     try {
//       String? token = await _getToken(); // Получение токена, реализуйте по своему
//       String base64File = base64Encode(await file.readAsBytes());
//
//       Map<String, dynamic> data = {
//         'file': base64File,
//       };
//
//       Response response = await dio.post(
//         'https://example.com/api/files/upload',
//         data: data,
//         options: Options(
//           headers: {
//             'Authorization': 'Bearer $token',
//             'Content-Type': 'application/json',
//           },
//         ),
//       );
//
//       if (response.statusCode == 200) {
//         fileName = response.data['name'];
//         print('File uploaded: $fileName');
//       } else {
//         print('Failed to upload file');
//       }
//     } catch (e) {
//       print('Error: $e');
//     }
//   }
//
//   Future<void> addDocument(String applicationId, String fileName) async {
//     try {
//       String? token = await _getToken(); // Получение токена, реализуйте по своему
//
//       Map<String, dynamic> data = {
//         'id': applicationId,
//         'title': 'Document Title', // Замените на реальное значение
//         'status': 'status', // Замените на реальное значение
//         'creation_date': DateTime.now().toUtc().toIso8601String(),
//         'expiration_date': DateTime.now().add(Duration(days: 30)).toUtc().toIso8601String(),
//         'file_name': fileName,
//       };
//
//       Response response = await dio.post(
//         'https://example.com/api/applications/$applicationId',
//         data: data,
//         options: Options(
//           headers: {
//             'Authorization': 'Bearer $token',
//             'Content-Type': 'application/json',
//           },
//         ),
//       );
//
//       if (response.statusCode == 200) {
//         print('Document added');
//       } else {
//         print('Failed to add document');
//       }
//     } catch (e) {
//       print('Error: $e');
//     }
//   }
//
//   Future<String?> _getToken() async {
//     // Реализуйте получение токена по своему усмотрению
//     return 'your_token_here';
//   }
//
//   Future<void> pickFile(FileSourceType sourceType) async {
//     XFile? xFile;
//     File? file;
//
//     if (sourceType == FileSourceType.gallery) {
//       xFile = await _picker.pickImage(source: ImageSource.gallery);
//     } else if (sourceType == FileSourceType.camera) {
//       xFile = await _picker.pickImage(source: ImageSource.camera);
//     } else if (sourceType == FileSourceType.device) {
//       FilePickerResult? result = await FilePicker.platform.pickFiles();
//       if (result != null && result.files.single.path != null) {
//         file = File(result.files.single.path!);
//       }
//     }
//
//     if (xFile != null) {
//       file = File(xFile.path);
//     }
//
//     if (file != null) {
//       await uploadFile(file);
//       if (fileName != null) {
//         await addDocument('your_application_id', fileName!); // Замените 'your_application_id' на реальный ID
//       }
//     } else {
//       print('No file selected');
//     }
//   }
//
//   void showFileSourceDialog(BuildContext context) {
//     showDialog(
//       context: context,
//       builder: (BuildContext context) {
//         return AlertDialog(
//           title: Text('Выберите источник файла'),
//           content: Column(
//             mainAxisSize: MainAxisSize.min,
//             children: <Widget>[
//               ListTile(
//                 leading: Icon(Icons.photo_library),
//                 title: Text('Выбрать из галереи'),
//                 onTap: () {
//                   Navigator.of(context).pop();
//                   pickFile(FileSourceType.gallery);
//                 },
//               ),
//               ListTile(
//                 leading: Icon(Icons.camera_alt),
//                 title: Text('Сделать снимок'),
//                 onTap: () {
//                   Navigator.of(context).pop();
//                   pickFile(FileSourceType.camera);
//                 },
//               ),
//               ListTile(
//                 leading: Icon(Icons.attach_file),
//                 title: Text('Выбрать с устройства'),
//                 onTap: () {
//                   Navigator.of(context).pop();
//                   pickFile(FileSourceType.device);
//                 },
//               ),
//             ],
//           ),
//         );
//       },
//     );
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text('Upload File Example'),
//       ),
//       body: Center(
//         child: ElevatedButton(
//           onPressed: () => showFileSourceDialog(context),
//           child: Text('Pick and Upload File'),
//         ),
//       ),
//     );
//   }
// }
//
// void main() => runApp(MaterialApp(
//       home: UploadFileScreen(),
//     ));
