#version 460

layout (location = 0) out vec4 out_color;
in vec3 position;
in vec4 color;

void main(){
    out_color = color;
}