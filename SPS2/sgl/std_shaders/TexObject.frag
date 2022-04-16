# version 460

in vec2 pass_textureCoords;
out vec4 out_Color;
uniform sampler2D texSampler;

void main(){
    out_Color = texture(texSampler, pass_textureCoords);
}