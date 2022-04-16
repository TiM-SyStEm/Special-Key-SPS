#version 460

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

void main()
{
    gl_Position = vec4(position, 1.0f);
    pass_textureCoords = textureCoords;
}