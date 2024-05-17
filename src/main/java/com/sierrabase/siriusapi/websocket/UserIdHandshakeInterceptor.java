package com.sierrabase.siriusapi.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class UserIdHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        String path = request.getURI().getPath();
//        String[] pathSegments = path.split("/");
//        if(pathSegments.length > 1) {
//            String loginId = pathSegments[1]; // login_id 추출
//            attributes.put("login_id", loginId); // WebSocket 세션에 login_id 추가
//        }
//
//        // HttpSession을 WebSocketSession의 속성으로 추가
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//            HttpSession httpSession = servletRequest.getServletRequest().getSession();
//            attributes.put("HTTP_SESSION", httpSession); // WebSocket 세션에 HTTP_SESSION 추가
//        }

        // URL 경로에서 loginId 추출 로직 활성화
        String path = request.getURI().getPath();
        String[] pathSegments = path.split("/");
        // URL 경로가 예상대로 분할되었는지 확인 (분할된 세그먼트 길이와 실제 loginId의 위치에 따라 조정 필요)
        // 예시는 URL 구조에 따라 달라질 수 있으므로, 실제 URL 패턴에 맞게 인덱스 조정 필요
        if (pathSegments.length > 1) {
            // URL에서 loginId 추출 (실제 경로와 세그먼트 위치에 따라 인덱스 조정 필요)
            String loginId = pathSegments[3];
            // WebSocket 세션 속성에 loginId 저장
            attributes.put("loginId", loginId);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 필요한 후처리를 여기에 작성
    }
}
