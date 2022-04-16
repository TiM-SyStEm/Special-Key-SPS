#version 460

layout (location = 0) in vec3 attrin_Position;
layout (location = 1) in vec4 attrin_Color;

out vec3 position;
out vec4 color;

void main(){
    position = attrin_Position;
    color = attrin_Color;
    gl_Position = vec4(attrin_Position, 1.0f);
}