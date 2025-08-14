import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init(){
        let path = try? FileManager.default.url(for: .applicationDirectory, in: .userDomainMask, appropriateFor: nil, create: true).appendingPathComponent("certs/cacert.pem")
        UnpackCerts.shared.extractCerts(toFile: path!.path)
        DIKt.doInitKoin(certsPath: path!.path)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}