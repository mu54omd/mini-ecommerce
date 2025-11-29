#!/bin/bash

IP=$(hostname -i | awk '{print $1}')

cat>frontend-gradle/composeApp/src/commonMain/kotlin/com/mu54omd/mini_ecommerce/frontend_gradle/config/GeneratedConfig.kt<<EOL
package com.mu54omd.mini_ecommerce.frontend_gradle.config

object GeneratedConfig {
    const val BASE_URL = "http://$IP:5050"
    const val BASE_URL_API_CLIENT = "\${BASE_URL}/api"
}
EOL

