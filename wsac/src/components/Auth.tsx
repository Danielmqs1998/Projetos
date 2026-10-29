import backgroundImg from '../assets/img/background.png';
import {
  AccountCircleRounded,
  ArrowUpward,
  HighlightOffRounded,
  Password,
} from '@mui/icons-material';
import { Button, IconButton, InputAdornment, TextField } from '@mui/material';
import { useNavigate } from 'react-router';

function Auth() {
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate('/home');
  };

  return (
    <div
      className="bg-cover bg-center min-h-screen w-full flex flex-col items-center justify-center overflow-hidden relative"
      style={{
        backgroundImage: `url(${backgroundImg})`,
        backgroundColor: '#1a1a1a',
      }}
    >
      <div className="absolute top-0 right-0 m-5">
        <div className="bg-transparent">
          <div className="flex justify-center items-center">
            <IconButton color="secondary" aria-label="fechar" size="large">
              <HighlightOffRounded />
            </IconButton>
          </div>
        </div>
      </div>
      <div className="mt-12 relative z-10">
        <div className="bg-transparent p-8 rounded-xl min-h-[320px] max-w-md w-full mx-4">
          <div className="flex flex-col gap-4">
            <div>
              <h1 className="m-5 text-xl font-bold tracking-tight text-black">
                <p className="rounded-xl mt-2 text-center text-black mb-2 bg-blue-500 py-1 px-3">
                  wSAC - Atendimento ao cliente
                </p>
              </h1>
            </div>
            <div className="w-full">
              <p className="text-center mb-2 text-black">Usuário</p>
              <TextField
                id="auth-username"
                variant="outlined"
                fullWidth
                slotProps={{
                  input: {
                    startAdornment: (
                      <InputAdornment position="start">
                        <AccountCircleRounded />
                      </InputAdornment>
                    ),
                  },
                  htmlInput: {
                    maxLength: 30,
                  },
                }}
              />
            </div>
            <div className="w-full">
              <p className="text-center mb-2 text-black">Senha</p>
              <TextField
                id="auth-password"
                variant="outlined"
                fullWidth
                type="password"
                slotProps={{
                  input: {
                    startAdornment: (
                      <InputAdornment position="start">
                        <Password />
                      </InputAdornment>
                    ),
                  },
                  htmlInput: {
                    maxLength: 30,
                  },
                }}
              />
            </div>
            <div className="flex justify-center mt-4">
              <Button
                onClick={handleLogin}
                className="min-w-[100px]"
                variant="contained"
                startIcon={<ArrowUpward />}
              >
                ACESSAR
              </Button>
            </div>
            <div className="w-full">
              <p className="text-center text-black/70">v1.0</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Auth;
