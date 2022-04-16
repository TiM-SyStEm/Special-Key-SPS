#version 460

layout (location = 0) out vec4 out_color;
uniform vec4 u_color;

void main(){
    out_color = vec4(u_color);
}