import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init(){
        do {
            let path = try FileManager.default.url(for: .applicationSupportDirectory, in: .userDomainMask, appropriateFor: nil, create: true).appendingPathComponent("certs/cacert.pem")
            UnpackCerts.shared.extractCerts(toFile: path.path)
            DIKt.doInitKoin(certsPath: path.path)
        }catch let e{
            print(e)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}