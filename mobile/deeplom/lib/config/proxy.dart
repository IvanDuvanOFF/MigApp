import 'dart:io';

import 'package:shared_preferences/shared_preferences.dart';

Future<CustomProxy> loadProxySettings() async {
  SharedPreferences prefs = await SharedPreferences.getInstance();
  String? proxy = prefs.getString('proxy');
  if (proxy != null) {
    return CustomProxy.fromString(proxy: proxy)!;
  } else {
    // Верните пустой прокси, если настройки не найдены
    return CustomProxy(ipAddress: '', port: 0);
  }
}

class MyHttpOverrides extends HttpOverrides {
  final List<int> certBytes;

  MyHttpOverrides(this.certBytes);

  @override
  HttpClient createHttpClient(SecurityContext? context) {
    SecurityContext securityContext = context ?? SecurityContext(withTrustedRoots: false);
    securityContext.setTrustedCertificatesBytes(certBytes);

    HttpClient client = super.createHttpClient(securityContext);
    client.badCertificateCallback = (X509Certificate cert, String host, int port) => true;
    return client;
  }
}

class CustomProxy {
  final String ipAddress;
  final int port;
  bool allowBadCertificates;

  CustomProxy({required this.ipAddress, required this.port, this.allowBadCertificates = false});

  static CustomProxy? fromString({required String proxy}) {
    if (proxy.isEmpty) {
      return null;
    }

    final proxyParts = proxy.split(":");
    final ipAddress = proxyParts[0];
    final port = proxyParts.length > 1 ? int.tryParse(proxyParts[1]) : 8888;
    return CustomProxy(
      ipAddress: ipAddress,
      port: port!,
    );
  }

  void enable() {
    if (ipAddress.isNotEmpty && port != 0) {
      HttpOverrides.global = CustomProxyHttpOverride.withProxy(toString(), allowBadCertificates: allowBadCertificates);
    }
  }

  void disable() {
    HttpOverrides.global = null;
  }

  @override
  String toString() {
    return "$ipAddress:$port";
  }
}

class CustomProxyHttpOverride extends HttpOverrides {
  final String proxyString;
  final bool allowBadCertificates;

  CustomProxyHttpOverride.withProxy(
      this.proxyString, {
        this.allowBadCertificates = false,
      });

  @override
  HttpClient createHttpClient(SecurityContext? context) {
    final client = super.createHttpClient(context);
    client.findProxy = (uri) {
      return "PROXY $proxyString;";
    };
    if (allowBadCertificates) {
      client.badCertificateCallback = (X509Certificate cert, String host, int port) => true;
    }
    return client;
  }
}
