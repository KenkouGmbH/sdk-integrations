import KenkouCoreMeasurement

class KenkouUtils {
    
    static func encodeData<T: Encodable>(data: T, success: (Any) -> (), failure: (Error) -> ()) {
        do {
            let jsonData = try JSONEncoder().encode(data)
            success(try JSONSerialization.jsonObject(with: jsonData, options: []))
        } catch let error {
            failure(error)
        }
    }
    
    static func decodeData<T: Decodable>(data: Any, returningClass: T.Type, success: (T) -> (), failure: (Error) -> ()) {
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: data, options: [])
            success(try JSONDecoder().decode(T.self, from: jsonData))
        } catch let error {
            failure(error)
        }
    }
}
