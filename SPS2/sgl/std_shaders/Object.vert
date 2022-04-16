#version 460

layout (location = 0) in vec3 attrin_Position;

void main(){
    gl_Position = vec4(attrin_Position, 1.0f);
}