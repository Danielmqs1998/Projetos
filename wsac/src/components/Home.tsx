import Box from '@mui/material/Box';
import {
  ChatConversation,
  ChatComposer,
  ChatComposerSendButton,
  ChatComposerTextArea,
  ChatComposerToolbar,
  ChatMessageList,
  createEchoAdapter,
} from '@mui/x-chat';
import { ChatProvider } from '@mui/x-chat/headless';
import type { ChatMessage, ChatUser } from '@mui/x-chat/headless';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';

const CONVERSATION_ID = 'custom-layout-conv';

const adapter = createEchoAdapter();

const demoUsers = {
  you: {
    id: 'you',
    displayName: 'You',
    avatarUrl: '',
    isOnline: true,
    role: 'user',
  } satisfies ChatUser,
  agent: {
    id: 'agent',
    displayName: 'MUI Assistant',
    avatarUrl: '',
    isOnline: true,
    role: 'assistant',
  } satisfies ChatUser,
} as const;

const contacts = [
  {
    id: 'agent',
    name: 'Suporte wSAC',
    role: 'assistant',
    status: 'online',
    avatarUrl: '',
  },
  {
    id: 'sales',
    name: 'Vendas',
    role: 'assistant',
    status: 'online',
    avatarUrl: '',
  },
  {
    id: 'billing',
    name: 'Cobrança',
    role: 'assistant',
    status: 'offline',
    avatarUrl: '',
  },
  {
    id: 'tech',
    name: 'Suporte Técnico',
    role: 'assistant',
    status: 'online',
    avatarUrl: '',
  },
];

const initialMessages: ChatMessage[] = [
  {
    id: 'cl-msg-1',
    conversationId: CONVERSATION_ID,
    role: 'assistant',
    author: demoUsers.agent,
    createdAt: '2026-03-15T09:58:00.000Z',
    parts: [
      {
        type: 'text',
        text: 'Olá! Sou o assistente wSAC. Como posso ajudar você hoje?',
      },
    ],
    status: 'sent',
  },
  {
    id: 'cl-msg-2',
    conversationId: CONVERSATION_ID,
    role: 'user',
    author: demoUsers.you,
    createdAt: '2026-03-15T10:00:00.000Z',
    parts: [
      {
        type: 'text',
        text: 'Bom dia! Gostaria de saber mais sobre os serviços.',
      },
    ],
    status: 'sent',
  },
];

function SendIcon() {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="currentColor"
      aria-hidden="true"
      style={{ width: '1em', height: '1em' }}
    >
      <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
    </svg>
  );
}

function ContactItem({ contact }: { contact: (typeof contacts)[number] }) {
  return (
    <ListItem disablePadding>
      <ListItemButton
        sx={{ py: 1, px: 1.5, '&:hover': { bgcolor: 'action.hover' } }}
      >
        <Avatar
          sx={{ mr: 1 }}
          src={contact.avatarUrl || ''}
          alt={contact.name}
        />
        <ListItemText
          primary={contact.name}
          secondary={contact.status === 'online' ? 'Online' : 'Offline'}
        />
      </ListItemButton>
      <Divider sx={{ mb: 0.5, mt: 0.5 }} />
    </ListItem>
  );
}

function CustomChat() {
  return (
    <ChatConversation>
      <ChatMessageList />
      <ChatComposer>
        <ChatComposerTextArea placeholder="Digite uma mensagem…" />
        <ChatComposerToolbar>
          <ChatComposerSendButton aria-label="Enviar mensagem">
            <SendIcon />
          </ChatComposerSendButton>
        </ChatComposerToolbar>
      </ChatComposer>
    </ChatConversation>
  );
}

export default function ChatProviderCustomLayout() {
  return (
    <ChatProvider
      adapter={adapter}
      initialActiveConversationId={CONVERSATION_ID}
      initialMessages={initialMessages}
    >
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          height: '100vh',
          minHeight: '100vh',
          boxSizing: 'border-box',
          '&, *::before, *::after': { boxSizing: 'inherit' },
        }}
      >
        <Box
          sx={{
            width: 280,
            flexShrink: 0,
            borderRight: 1,
            borderColor: 'divider',
            backgroundColor: 'background.paper',
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <Box
            sx={{
              p: 2,
              borderBottom: 1,
              borderColor: 'divider',
              display: 'flex',
              alignItems: 'center',
              gap: 1,
            }}
          >
            <Avatar sx={{ bgcolor: 'primary.main' }} src="" alt="wSAC" />
            <Typography component="h6" variant="h6" sx={{ fontWeight: 'bold' }}>
              wSAC
            </Typography>
          </Box>
          <Box
            sx={{
              flex: 1,
              overflow: 'hidden',
              display: 'flex',
              flexDirection: 'column',
            }}
          >
            <Typography
              component="span"
              variant="subtitle2"
              sx={{ px: 2, py: 1, color: 'text.secondary' }}
            >
              Contatos
            </Typography>
            <List sx={{ flex: 1, overflow: 'auto', pb: 2 }}>
              {contacts.map((contact) => (
                <ContactItem key={contact.id} contact={contact} />
              ))}
            </List>
          </Box>
        </Box>
        <Box
          sx={{
            flex: 1,
            display: 'flex',
            flexDirection: 'column',
            minWidth: 0,
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 0,
            overflow: 'hidden',
            boxSizing: 'border-box',
          }} >
          <CustomChat />
        </Box>
      </Box>
    </ChatProvider>
  );
}
